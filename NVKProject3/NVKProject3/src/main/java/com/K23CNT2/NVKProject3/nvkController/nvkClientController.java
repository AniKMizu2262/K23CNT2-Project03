package com.K23CNT2.NVKProject3.nvkController;

import com.K23CNT2.NVKProject3.nvkDto.nvkCartItem;
import com.K23CNT2.NVKProject3.nvkEntity.*;
import com.K23CNT2.NVKProject3.nvkRepository.*;
import com.K23CNT2.NVKProject3.nvkService.nvkCartService;
import com.K23CNT2.NVKProject3.nvkService.nvkCategoryService;
import com.K23CNT2.NVKProject3.nvkService.nvkCustomerService;
import com.K23CNT2.NVKProject3.nvkService.nvkProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class nvkClientController {

    @Autowired
    private nvkCustomerService nvkCustomerService;
    @Autowired
    private nvkCustomerRepository nvkCustomerRepo;
    @Autowired
    private nvkProductService productService;
    @Autowired
    private nvkProductRepository nvkProductRepo;
    @Autowired
    private nvkReviewRepository nvkReviewRepo;
    @Autowired
    private nvkCategoryService categoryService;
    @Autowired
    private nvkCategoryRepository nvkCategoryRepo;
    @Autowired
    private nvkCartService cartService;
    @Autowired
    private nvkOrderRepository orderRepo;
    @Autowired
    private nvkOrderDetailRepository orderDetailRepo;

    // ==============================================================
    // [HELPER] Cập nhật số lượng giỏ hàng vào Session (RAM)
    // ==============================================================
    private void updateCartCountSession(HttpSession session) {
        int count = cartService.getCart(session).stream()
                .mapToInt(nvkCartItem::getQuantity).sum();
        session.setAttribute("sessionCartCount", count);
    }

    // ==============================================================
    // 1. MODEL DÙNG CHUNG
    // ==============================================================
    @ModelAttribute("nvkCategories")
    public List<nvkCategory> getAllCategories() {
        return categoryService.getAllCategories();
    }

    // [TỐI ƯU HÓA SIÊU TỐC]: Lấy số lượng từ Session (RAM)
    @GetMapping("/api/nvk-cart/count")
    @ResponseBody
    public Map<String, Integer> getCartCountApi(HttpSession session) {
        // 1. Lấy từ Session cho nhanh
        Integer count = (Integer) session.getAttribute("sessionCartCount");

        // 2. Nếu chưa có thì tính toán lại rồi lưu cache
        if (count == null) {
            if (session.getAttribute("nvkUserLogin") == null) {
                count = 0;
            } else {
                count = cartService.getCart(session).stream()
                        .mapToInt(nvkCartItem::getQuantity).sum();
            }
            session.setAttribute("sessionCartCount", count);
        }

        // Trả về JSON: { "count": 5 }
        return Collections.singletonMap("count", count);
    }

    // ==============================================================
    // 2. CÁC TRANG CƠ BẢN
    // ==============================================================
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("nvkProducts", productService.getAllProducts());
        model.addAttribute("listRandom", productService.findRandomProducts());
        return "index";
    }

    @GetMapping("/nvk-login")
    public String showLogin() {
        return "client/login";
    }

    @PostMapping("/nvk-login")
    public String loginSubmit(@RequestParam("nvkEmail") String email,
                              @RequestParam("nvkPassword") String password,
                              HttpSession session, Model model) {
        nvkCustomer customer = nvkCustomerService.login(email, password);
        if (customer != null) {
            if (customer.getNvkActive() == null || !customer.getNvkActive()) {
                model.addAttribute("error", "Tài khoản bị khóa/chưa kích hoạt!");
                return "client/login";
            }
            session.setAttribute("nvkUserLogin", customer);

            // Tính lại giỏ hàng 1 lần khi vừa đăng nhập để lưu vào RAM
            updateCartCountSession(session);

            return "redirect:/";
        }
        model.addAttribute("error", "Sai email hoặc mật khẩu!");
        return "client/login";
    }

    @GetMapping("/nvk-logout")
    public String logout(HttpSession session) {
        session.removeAttribute("nvkUserLogin");
        session.removeAttribute("sessionCartCount"); // Xóa cache
        return "redirect:/";
    }

    @GetMapping("/nvk-register")
    public String showRegister(Model model) {
        model.addAttribute("nvkCustomer", new nvkCustomer());
        return "client/register";
    }

    @PostMapping("/nvk-register")
    public String registerSubmit(@ModelAttribute("nvkCustomer") nvkCustomer customer, Model model) {
        if (nvkCustomerRepo.findByNvkEmail(customer.getNvkEmail()) != null) {
            model.addAttribute("error", "Email đã tồn tại!");
            return "client/register";
        }
        customer.setNvkActive(true);
        nvkCustomerRepo.save(customer);
        return "redirect:/nvk-login?success=true";
    }

    @GetMapping("/nvk-product/detail/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model) {
        nvkProduct product = productService.getProductById(id);
        if (product == null) return "redirect:/";
        model.addAttribute("nvkProduct", product);
        return "client/nvk-product-detail";
    }

    // ==============================================================
    // 3. CHỨC NĂNG GIỎ HÀNG
    // ==============================================================
    @PostMapping("/nvk-cart/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam("quantity") int quantity,
                            HttpServletRequest request,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        if (session.getAttribute("nvkUserLogin") == null)
            return "redirect:/nvk-login?error=login_required_cart";

        nvkProduct product = productService.getProductById(productId);
        if (product != null) {
            int currentStock = (product.getNvkQuantity() != null) ? product.getNvkQuantity() : 0;
            if (quantity > currentStock) {
                redirectAttributes.addFlashAttribute("error", "Vượt quá tồn kho (Còn: " + currentStock + ")");
                String referer = request.getHeader("Referer");
                return "redirect:" + (referer != null ? referer : "/");
            }
        }

        cartService.addToCart(productId, quantity, session);

        // Cập nhật RAM ngay sau khi thêm
        updateCartCountSession(session);

        String referer = request.getHeader("Referer");
        if (referer == null) return "redirect:/?success=added";
        return "redirect:" + referer + (referer.contains("?") ? "&" : "?") + "success=added";
    }

    @GetMapping("/nvk-cart")
    public String viewCart(HttpSession session, Model model) {
        if (session.getAttribute("nvkUserLogin") == null) return "redirect:/nvk-login";
        model.addAttribute("cartItems", cartService.getCart(session));
        model.addAttribute("totalAmount", cartService.getTotalAmount(session));
        return "client/nvk-cart";
    }

    @GetMapping("/nvk-cart/remove/{id}")
    public String removeFromCart(@PathVariable("id") Long productId, HttpSession session) {
        cartService.removeFromCart(productId, session);
        // Cập nhật RAM ngay sau khi xóa
        updateCartCountSession(session);
        return "redirect:/nvk-cart";
    }

    @PostMapping("/nvk-cart/checkout")
    public String checkout(@RequestParam("receiverName") String receiverName,
                           @RequestParam("receiverPhone") String receiverPhone,
                           @RequestParam("receiverAddress") String receiverAddress,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {

        nvkCustomer currentUser = (nvkCustomer) session.getAttribute("nvkUserLogin");
        if (currentUser == null) return "redirect:/nvk-login";

        List<nvkCartItem> cart = cartService.getCart(session);
        if (cart.isEmpty()) return "redirect:/nvk-cart";

        for (nvkCartItem item : cart) {
            nvkProduct p = productService.getProductById(item.getProductId());
            if (p == null || (p.getNvkQuantity() != null && item.getQuantity() > p.getNvkQuantity())) {
                String name = (p != null) ? p.getNvkName() : "Sản phẩm";
                redirectAttributes.addFlashAttribute("error", "Sản phẩm [" + name + "] không đủ số lượng!");
                return "redirect:/nvk-cart";
            }
        }

        nvkOrder order = new nvkOrder();
        order.setNvkCustomer(currentUser);
        order.setNvkCreatedDate(LocalDateTime.now());
        order.setNvkStatus(0);
        order.setNvkTotalAmount(cartService.getTotalAmount(session));
        order.setNvkReceiverName(receiverName);
        order.setNvkReceiverPhone(receiverPhone);
        order.setNvkReceiverAddress(receiverAddress);
        order.setNvkCode("ORD-" + System.currentTimeMillis());

        nvkOrder savedOrder = orderRepo.save(order);

        for (nvkCartItem item : cart) {
            nvkOrderDetail detail = new nvkOrderDetail();
            detail.setNvkOrder(savedOrder);
            nvkProduct product = productService.getProductById(item.getProductId());
            detail.setNvkProduct(product);
            detail.setNvkQuantity(item.getQuantity());
            detail.setNvkPrice(item.getPrice());
            orderDetailRepo.save(detail);

            if (product != null) {
                int currentStock = (product.getNvkQuantity() != null) ? product.getNvkQuantity() : 0;
                int newQty = Math.max(0, currentStock - item.getQuantity());
                product.setNvkQuantity(newQty);
                if (newQty == 0) product.setNvkStatus(0);
                productService.updateProduct(product);
            }
        }

        cartService.clearCart(session);
        // Reset RAM về 0
        session.setAttribute("sessionCartCount", 0);

        return "redirect:/nvk-order/history?success=order_placed";
    }

    // ==============================================================
    // 4. API AJAX
    // ==============================================================
    @PostMapping("/api/nvk-cart/add")
    @ResponseBody
    public Map<String, Object> addToCartAjax(@RequestParam("productId") Long productId,
                                             @RequestParam("quantity") int quantity,
                                             HttpSession session) {
        Map<String, Object> res = new HashMap<>();
        if (session.getAttribute("nvkUserLogin") == null) {
            res.put("status", "login_required");
            return res;
        }

        nvkProduct product = productService.getProductById(productId);
        if (product != null) {
            int currentStock = (product.getNvkQuantity() != null) ? product.getNvkQuantity() : 0;
            if (quantity > currentStock) {
                res.put("status", "error");
                res.put("message", "Vượt quá tồn kho!");
                return res;
            }
        }

        cartService.addToCart(productId, quantity, session);
        updateCartCountSession(session);

        int newCount = (int) session.getAttribute("sessionCartCount");
        res.put("status", "success");
        res.put("cartCount", newCount);
        return res;
    }

    @PostMapping("/api/nvk-review/submit")
    @ResponseBody
    public Map<String, Object> submitReviewAjax(@RequestParam("productId") Long productId,
                                                @RequestParam("rating") Integer rating,
                                                @RequestParam("reviewComment") String comment,
                                                HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        nvkCustomer currentUser = (nvkCustomer) session.getAttribute("nvkUserLogin");
        if (currentUser == null) {
            response.put("status", "login_required");
            return response;
        }
        nvkProduct product = productService.getProductById(productId);
        if (product == null) {
            response.put("status", "error");
            return response;
        }
        nvkReview review = new nvkReview();
        review.setNvkProduct(product);
        review.setNvkCustomer(currentUser);
        review.setNvkRating(rating);
        review.setNvkComment(comment);
        review.setNvkCreatedDate(LocalDate.now());
        nvkReviewRepo.save(review);
        response.put("avatarUrl", currentUser.getNvkAvatar());

        response.put("status", "success");
        response.put("userName", currentUser.getNvkFullName());
        response.put("comment", comment);
        response.put("rating", rating);
        return response;
    }

    // ==============================================================
    // 5. PROFILE (Đã fix Path User)
    // ==============================================================
    @GetMapping("/nvk-profile")
    public String userProfile(Model model, HttpSession session) {
        nvkCustomer userLogin = (nvkCustomer) session.getAttribute("nvkUserLogin");
        if (userLogin == null) return "redirect:/nvk-login";
        nvkCustomer currentUser = nvkCustomerService.getCustomerById(userLogin.getNvkId());
        session.setAttribute("nvkUserLogin", currentUser);
        model.addAttribute("nvkCustomer", currentUser);
        return "client/nvk-profile";
    }

    @PostMapping("/nvk-profile/update")
    public String updateProfileSubmit(@ModelAttribute("nvkCustomer") nvkCustomer customerData,
                                      @RequestParam(value = "nvkImageFile", required = false) MultipartFile file,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        nvkCustomer currentUser = (nvkCustomer) session.getAttribute("nvkUserLogin");
        if (currentUser == null) return "redirect:/nvk-login";
        try {
            if (file != null && !file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                String rootPath = System.getProperty("user.dir");
                Path uploadPath = Paths.get(rootPath, "uploads", "user");
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                }
                currentUser.setNvkAvatar("/nvk-images/" + fileName);
            }
            currentUser.setNvkFullName(customerData.getNvkFullName());
            currentUser.setNvkPhone(customerData.getNvkPhone());
            currentUser.setNvkAddress(customerData.getNvkAddress());
            nvkCustomerService.updateCustomer(currentUser);
            session.setAttribute("nvkUserLogin", currentUser);
            redirectAttributes.addFlashAttribute("success", "Cập nhật hồ sơ thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/nvk-profile";
    }

    @PostMapping("/nvk-profile/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 HttpSession session) {
        nvkCustomer currentUser = (nvkCustomer) session.getAttribute("nvkUserLogin");
        if (currentUser == null) return "redirect:/nvk-login";
        if (!newPassword.equals(confirmPassword)) return "redirect:/nvk-profile?error=pass_mismatch";
        boolean isChanged = nvkCustomerService.changePassword(currentUser.getNvkId(), currentPassword, newPassword);
        return isChanged ? "redirect:/nvk-profile?success=pass_changed" : "redirect:/nvk-profile?error=wrong_old_pass";
    }

    // ==============================================================
    // 6. ORDER & CATEGORIES
    // ==============================================================
    @GetMapping("/nvk-order/history")
    public String orderHistory(HttpSession session, Model model) {
        nvkCustomer currentUser = (nvkCustomer) session.getAttribute("nvkUserLogin");
        if (currentUser == null) return "redirect:/nvk-login";
        List<nvkOrder> orders = orderRepo.findByNvkCustomerOrderByNvkCreatedDateDesc(currentUser);
        model.addAttribute("nvkOrders", orders);
        return "client/nvk-order-history";
    }

    @GetMapping("/nvk-order/detail/{id}")
    public String orderDetail(@PathVariable("id") Long id, HttpSession session, Model model) {
        nvkCustomer currentUser = (nvkCustomer) session.getAttribute("nvkUserLogin");
        if (currentUser == null) return "redirect:/nvk-login";
        nvkOrder order = orderRepo.findById(id).orElse(null);
        if (order == null || !order.getNvkCustomer().getNvkId().equals(currentUser.getNvkId()))
            return "redirect:/nvk-order/history";
        model.addAttribute("nvkOrder", order);
        model.addAttribute("nvkOrderDetails", orderDetailRepo.findByNvkOrder(order));
        return "client/nvk-order-detail";
    }

    @GetMapping("/nvk-order/cancel/{id}")
    public String cancelOrder(@PathVariable("id") Long id, HttpSession session) {
        nvkCustomer currentUser = (nvkCustomer) session.getAttribute("nvkUserLogin");
        if (currentUser == null) return "redirect:/nvk-login";
        nvkOrder order = orderRepo.findById(id).orElse(null);
        if (order != null && order.getNvkCustomer().getNvkId().equals(currentUser.getNvkId()) && order.getNvkStatus() == 0) {
            order.setNvkStatus(4);
            orderRepo.save(order);
            List<nvkOrderDetail> details = orderDetailRepo.findByNvkOrder(order);
            for (nvkOrderDetail detail : details) {
                nvkProduct product = detail.getNvkProduct();
                if (product != null) {
                    product.setNvkQuantity((product.getNvkQuantity() != null ? product.getNvkQuantity() : 0) + detail.getNvkQuantity());
                    if (product.getNvkStatus() == 0) product.setNvkStatus(1);
                    productService.updateProduct(product);
                }
            }
        }
        return "redirect:/nvk-order/history?success=cancelled";
    }

    @GetMapping("/nvk-categories")
    public String showCategoriesPage(Model model,
                                     @RequestParam(name = "cid", required = false) Long cid,
                                     @RequestParam(name = "sort", required = false) String sort,
                                     @RequestParam(name = "instock", required = false) Boolean instock,
                                     @RequestParam(name = "page", defaultValue = "0") int page) {
        List<nvkProduct> allProducts = productService.getAllProducts();
        if (cid != null)
            allProducts = allProducts.stream().filter(p -> p.getNvkCategory() != null && p.getNvkCategory().getNvkId().equals(cid)).collect(Collectors.toList());
        if (instock != null && instock)
            allProducts = allProducts.stream().filter(p -> p.getNvkQuantity() != null && p.getNvkQuantity() > 0).collect(Collectors.toList());
        if (sort != null) {
            switch (sort) {
                case "price_asc":
                    allProducts.sort(Comparator.comparing(nvkProduct::getNvkPrice, Comparator.nullsLast(Comparator.naturalOrder())));
                    break;
                case "price_desc":
                    allProducts.sort(Comparator.comparing(nvkProduct::getNvkPrice, Comparator.nullsLast(Comparator.reverseOrder())));
                    break;
                case "name_asc":
                    allProducts.sort(Comparator.comparing(nvkProduct::getNvkName));
                    break;
                case "name_desc":
                    allProducts.sort(Comparator.comparing(nvkProduct::getNvkName).reversed());
                    break;
            }
        }
        int pageSize = 9;
        int totalItems = allProducts.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        if (page < 0) page = 0;
        if (page >= totalPages && totalPages > 0) page = totalPages - 1;
        int start = page * pageSize;
        int end = Math.min(start + pageSize, totalItems);
        model.addAttribute("nvkProducts", (start > end || start >= totalItems) ? new ArrayList<>() : allProducts.subList(start, end));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("cid", cid);
        model.addAttribute("sort", sort);
        model.addAttribute("instock", instock);
        return "client/nvk-category-list";
    }

    @GetMapping("/nvk-search")
    public String searchProduct(@RequestParam("keyword") String keyword, Model model) {
        List<nvkProduct> products = (keyword != null && !keyword.trim().isEmpty())
                ? nvkProductRepo.findByNvkNameContainingIgnoreCase(keyword)
                : nvkProductRepo.findAll();
        model.addAttribute("nvkProducts", products);
        model.addAttribute("totalItems", products.size());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);
        return "client/nvk-category-list";
    }
}