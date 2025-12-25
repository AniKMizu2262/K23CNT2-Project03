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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class nvkClientController {

    // --- SERVICES & REPOSITORIES ---
    @Autowired
    private nvkCustomerService customerService;
    @Autowired
    private nvkCustomerRepository customerRepo;
    @Autowired
    private nvkProductService productService;
    @Autowired
    private nvkProductRepository productRepo;
    @Autowired
    private nvkReviewRepository reviewRepo;
    @Autowired
    private nvkCategoryService categoryService;
    @Autowired
    private nvkCartService cartService;
    @Autowired
    private nvkOrderRepository orderRepo;
    @Autowired
    private nvkOrderDetailRepository orderDetailRepo;

    // --- HELPER METHODS ---

    // Cập nhật số lượng giỏ hàng vào Session
    private void updateCartCountSession(HttpSession session) {
        int count = cartService.getCart(session).stream()
                .mapToInt(nvkCartItem::getQuantity).sum();
        session.setAttribute("sessionCartCount", count);
    }

    // Luôn load danh mục cho menu
    @ModelAttribute("nvkCategories")
    public List<nvkCategory> getAllCategories() {
        return categoryService.getAllCategories();
    }

    // ==============================================================
    // 1. TRANG CHỦ & AUTH (LOGIN/REGISTER)
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
        nvkCustomer customer = customerService.login(email, password);
        if (customer != null) {
            if (customer.getNvkActive() == null || !customer.getNvkActive()) {
                model.addAttribute("error", "Tài khoản của bạn đang bị khóa!");
                return "client/login";
            }
            session.setAttribute("nvkUserLogin", customer);
            updateCartCountSession(session); // Load giỏ hàng
            return "redirect:/";
        }
        model.addAttribute("error", "Sai email hoặc mật khẩu!");
        return "client/login";
    }

    @GetMapping("/nvk-logout")
    public String logout(HttpSession session) {
        session.removeAttribute("nvkUserLogin");
        session.removeAttribute("sessionCartCount");
        return "redirect:/";
    }

    @GetMapping("/nvk-register")
    public String showRegister(Model model) {
        model.addAttribute("nvkCustomer", new nvkCustomer());
        return "client/register";
    }

    @PostMapping("/nvk-register")
    public String registerSubmit(@ModelAttribute("nvkCustomer") nvkCustomer customer, Model model) {
        if (customerRepo.findByNvkEmail(customer.getNvkEmail()) != null) {
            model.addAttribute("error", "Email đã tồn tại!");
            return "client/register";
        }
        customer.setNvkActive(true);
        customerRepo.save(customer);
        return "redirect:/nvk-login?success=true";
    }

    // ==============================================================
    // 2. SẢN PHẨM & DANH MỤC
    // ==============================================================

    @GetMapping("/nvk-product/detail/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model) {
        nvkProduct product = productService.getProductById(id);
        if (product == null) return "redirect:/";
        model.addAttribute("nvkProduct", product);
        return "client/nvk-product-detail";
    }

    @GetMapping("/nvk-categories")
    public String showCategoriesPage(Model model,
                                     @RequestParam(name = "cid", required = false) Long cid,
                                     @RequestParam(name = "sort", required = false) String sort,
                                     @RequestParam(name = "instock", required = false) Boolean instock,
                                     @RequestParam(name = "page", defaultValue = "0") int page) {

        List<nvkProduct> allProducts = productService.filterProducts(cid, sort, instock != null && instock);

        // Phân trang thủ công (9 sản phẩm/trang)
        int pageSize = 9;
        int totalItems = allProducts.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        if (page < 0) page = 0;
        if (page >= totalPages && totalPages > 0) page = totalPages - 1;

        int start = page * pageSize;
        int end = Math.min(start + pageSize, totalItems);

        model.addAttribute("nvkProducts", (start > end) ? new ArrayList<>() : allProducts.subList(start, end));
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
        List<nvkProduct> products = productService.searchProducts(keyword);
        model.addAttribute("nvkProducts", products);
        model.addAttribute("totalItems", products.size());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);
        return "client/nvk-category-list";
    }

    // ==============================================================
    // 3. GIỎ HÀNG (CART)
    // ==============================================================

    @PostMapping("/nvk-cart/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam("quantity") int quantity,
                            HttpServletRequest request,
                            HttpSession session,
                            RedirectAttributes ra) {
        if (session.getAttribute("nvkUserLogin") == null)
            return "redirect:/nvk-login?error=login_required_cart";

        // Check tồn kho
        nvkProduct product = productService.getProductById(productId);
        if (product != null && quantity > product.getNvkQuantity()) {
            ra.addFlashAttribute("error", "Vượt quá tồn kho!");
            String referer = request.getHeader("Referer");
            return "redirect:" + (referer != null ? referer : "/");
        }

        cartService.addToCart(productId, quantity, session);
        updateCartCountSession(session);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/") + "?success=added";
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
        updateCartCountSession(session);
        return "redirect:/nvk-cart";
    }

    @PostMapping("/nvk-cart/checkout")
    public String checkout(@RequestParam("receiverName") String receiverName,
                           @RequestParam("receiverPhone") String receiverPhone,
                           @RequestParam("receiverAddress") String receiverAddress,
                           HttpSession session, RedirectAttributes ra) {

        nvkCustomer currentUser = (nvkCustomer) session.getAttribute("nvkUserLogin");
        if (currentUser == null) return "redirect:/nvk-login";

        List<nvkCartItem> cart = cartService.getCart(session);
        if (cart.isEmpty()) return "redirect:/nvk-cart";

        // Tạo đơn hàng
        nvkOrder order = new nvkOrder();
        order.setNvkCustomer(currentUser);
        order.setNvkCreatedDate(LocalDateTime.now());
        order.setNvkStatus(0); // Mới đặt
        order.setNvkTotalAmount(cartService.getTotalAmount(session));
        order.setNvkReceiverName(receiverName);
        order.setNvkReceiverPhone(receiverPhone);
        order.setNvkReceiverAddress(receiverAddress);
        order.setNvkCode("ORD-" + System.currentTimeMillis());

        nvkOrder savedOrder = orderRepo.save(order);

        // Lưu chi tiết & Trừ tồn kho
        for (nvkCartItem item : cart) {
            nvkOrderDetail detail = new nvkOrderDetail();
            detail.setNvkOrder(savedOrder);
            nvkProduct product = productService.getProductById(item.getProductId());
            detail.setNvkProduct(product);
            detail.setNvkQuantity(item.getQuantity());
            detail.setNvkPrice(item.getPrice());
            orderDetailRepo.save(detail);

            if (product != null) {
                int newQty = Math.max(0, product.getNvkQuantity() - item.getQuantity());
                product.setNvkQuantity(newQty);
                if (newQty == 0) product.setNvkStatus(0); // Hết hàng -> Ngừng bán
                productService.updateProduct(product);
            }
        }

        cartService.clearCart(session);
        session.setAttribute("sessionCartCount", 0);

        return "redirect:/nvk-order/history?success=order_placed";
    }

    // ==============================================================
    // 4. API AJAX (CART & REVIEW)
    // ==============================================================

    // API lấy số lượng giỏ hàng
    @GetMapping("/api/nvk-cart/count")
    @ResponseBody
    public Map<String, Integer> getCartCountApi(HttpSession session) {
        Integer count = (Integer) session.getAttribute("sessionCartCount");
        if (count == null) count = 0;
        return Collections.singletonMap("count", count);
    }

    // API Thêm vào giỏ (AJAX)
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
        if (product != null && quantity > product.getNvkQuantity()) {
            res.put("status", "error");
            res.put("message", "Vượt quá tồn kho!");
            return res;
        }

        cartService.addToCart(productId, quantity, session);
        updateCartCountSession(session);

        res.put("status", "success");
        res.put("cartCount", session.getAttribute("sessionCartCount"));
        return res;
    }

    // API Gửi đánh giá
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
        if (product != null) {
            nvkReview review = new nvkReview();
            review.setNvkProduct(product);
            review.setNvkCustomer(currentUser);
            review.setNvkRating(rating);
            review.setNvkComment(comment);
            review.setNvkCreatedDate(LocalDate.now());
            reviewRepo.save(review);

            response.put("status", "success");
            response.put("userName", currentUser.getNvkFullName());
            response.put("avatarUrl", currentUser.getNvkAvatar());
            response.put("comment", comment);
            response.put("rating", rating);
        } else {
            response.put("status", "error");
        }
        return response;
    }

    // ==============================================================
    // 5. PROFILE & ORDER HISTORY
    // ==============================================================

    @GetMapping("/nvk-profile")
    public String userProfile(Model model, HttpSession session) {
        nvkCustomer user = (nvkCustomer) session.getAttribute("nvkUserLogin");
        if (user == null) return "redirect:/nvk-login";

        // Lấy lại từ DB để có dữ liệu mới nhất
        model.addAttribute("nvkCustomer", customerService.getCustomerById(user.getNvkId()));
        return "client/nvk-profile";
    }

    @PostMapping("/nvk-profile/update")
    public String updateProfileSubmit(@ModelAttribute("nvkCustomer") nvkCustomer customerData,
                                      @RequestParam(value = "nvkImageFile", required = false) MultipartFile file,
                                      HttpSession session, RedirectAttributes ra) {
        nvkCustomer user = (nvkCustomer) session.getAttribute("nvkUserLogin");
        if (user == null) return "redirect:/nvk-login";

        try {
            if (file != null && !file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path path = Paths.get("uploads", "user");
                if (!Files.exists(path)) Files.createDirectories(path);
                Files.copy(file.getInputStream(), path.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                customerData.setNvkAvatar("/nvk-images/" + fileName);
            }

            // Gán ID để biết update ai
            customerData.setNvkId(user.getNvkId());

            nvkCustomer updatedUser = customerService.updateCustomer(customerData);
            session.setAttribute("nvkUserLogin", updatedUser);
            ra.addFlashAttribute("success", "Cập nhật hồ sơ thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/nvk-profile";
    }

    @PostMapping("/nvk-profile/change-password")
    public String changePassword(@RequestParam("currentPassword") String current,
                                 @RequestParam("newPassword") String newPass,
                                 @RequestParam("confirmPassword") String confirm,
                                 HttpSession session) {
        nvkCustomer user = (nvkCustomer) session.getAttribute("nvkUserLogin");
        if (user == null) return "redirect:/nvk-login";

        if (!newPass.equals(confirm)) return "redirect:/nvk-profile?error=pass_mismatch";

        boolean ok = customerService.changePassword(user.getNvkId(), current, newPass);
        return ok ? "redirect:/nvk-profile?success=pass_changed" : "redirect:/nvk-profile?error=wrong_old_pass";
    }

    @GetMapping("/nvk-order/history")
    public String orderHistory(HttpSession session, Model model) {
        nvkCustomer user = (nvkCustomer) session.getAttribute("nvkUserLogin");
        if (user == null) return "redirect:/nvk-login";

        model.addAttribute("nvkOrders", orderRepo.findByNvkCustomerOrderByNvkCreatedDateDesc(user));
        return "client/nvk-order-history";
    }

    @GetMapping("/nvk-order/detail/{id}")
    public String clientOrderDetail(@PathVariable("id") Long id, HttpSession session, Model model) {
        nvkCustomer user = (nvkCustomer) session.getAttribute("nvkUserLogin");
        if (user == null) return "redirect:/nvk-login";

        nvkOrder order = orderRepo.findById(id).orElse(null);
        if (order == null || !order.getNvkCustomer().getNvkId().equals(user.getNvkId()))
            return "redirect:/nvk-order/history";

        model.addAttribute("nvkOrder", order);
        model.addAttribute("nvkOrderDetails", orderDetailRepo.findByNvkOrder(order));
        return "client/nvk-order-detail";
    }

    @GetMapping("/nvk-order/cancel/{id}")
    public String cancelOrder(@PathVariable("id") Long id, HttpSession session) {
        nvkCustomer user = (nvkCustomer) session.getAttribute("nvkUserLogin");
        if (user == null) return "redirect:/nvk-login";

        nvkOrder order = orderRepo.findById(id).orElse(null);
        if (order != null && order.getNvkCustomer().getNvkId().equals(user.getNvkId()) && order.getNvkStatus() == 0) {
            order.setNvkStatus(4); // Hủy
            orderRepo.save(order);

            // Hoàn lại số lượng tồn kho
            List<nvkOrderDetail> details = orderDetailRepo.findByNvkOrder(order);
            for (nvkOrderDetail d : details) {
                nvkProduct p = d.getNvkProduct();
                if (p != null) {
                    p.setNvkQuantity(p.getNvkQuantity() + d.getNvkQuantity());
                    if (p.getNvkStatus() == 0) p.setNvkStatus(1);
                    productService.updateProduct(p);
                }
            }
        }
        return "redirect:/nvk-order/history?success=cancelled";
    }
}