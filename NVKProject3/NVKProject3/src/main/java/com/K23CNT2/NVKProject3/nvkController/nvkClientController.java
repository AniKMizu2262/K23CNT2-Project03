package com.K23CNT2.NVKProject3.nvkController;

import com.K23CNT2.NVKProject3.nvkDto.nvkCartItem;
import com.K23CNT2.NVKProject3.nvkEntity.*;
import com.K23CNT2.NVKProject3.nvkRepository.nvkCustomerRepository;
import com.K23CNT2.NVKProject3.nvkRepository.nvkOrderDetailRepository;
import com.K23CNT2.NVKProject3.nvkRepository.nvkOrderRepository;
import com.K23CNT2.NVKProject3.nvkRepository.nvkReviewRepository;
import com.K23CNT2.NVKProject3.nvkService.nvkCartService;
import com.K23CNT2.NVKProject3.nvkService.nvkCategoryService;
import com.K23CNT2.NVKProject3.nvkService.nvkCustomerService;
import com.K23CNT2.NVKProject3.nvkService.nvkProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class nvkClientController {

    // ==============================================================
    // 0. KHAI BÁO SERVICE & REPO
    // ==============================================================
    @Autowired
    private nvkCustomerService nvkCustomerService;
    @Autowired
    private nvkCustomerRepository nvkCustomerRepo;
    @Autowired
    private nvkProductService productService;
    @Autowired
    private nvkReviewRepository nvkReviewRepo;
    @Autowired
    private nvkCategoryService categoryService;
    @Autowired
    private nvkCartService cartService;
    @Autowired
    private nvkOrderRepository orderRepo;
    @Autowired
    private nvkOrderDetailRepository orderDetailRepo;

    // Đã XÓA nvkProductRegisterRepository theo yêu cầu

    // ==============================================================
    // 1. MODEL DÙNG CHUNG (MENU, GIỎ HÀNG...)
    // ==============================================================
    @ModelAttribute("nvkCategories")
    public List<nvkCategory> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @ModelAttribute("cartCount")
    public int getCartCount(HttpSession session) {
        if (session.getAttribute("nvkUserLogin") == null) return 0;
        return cartService.getCart(session).stream().mapToInt(nvkCartItem::getQuantity).sum();
    }

    // ==============================================================
    // 2. TRANG CHỦ
    // ==============================================================
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("nvkProducts", productService.getAllProducts());
        List<nvkProduct> listRandom = productService.findRandomProducts();
        model.addAttribute("listRandom", listRandom);
        return "index";
    }

    // ==============================================================
    // 3. ĐĂNG NHẬP – ĐĂNG KÝ – ĐĂNG XUẤT
    // ==============================================================
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
            if (!customer.getNvkActive()) {
                model.addAttribute("error", "Tài khoản đang bị khóa!");
                return "client/login";
            }
            session.setAttribute("nvkUserLogin", customer);
            return "redirect:/";
        }
        model.addAttribute("error", "Email hoặc mật khẩu không đúng!");
        return "client/login";
    }

    @GetMapping("/nvk-logout")
    public String logout(HttpSession session) {
        session.removeAttribute("nvkUserLogin");
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
            model.addAttribute("error", "Email này đã tồn tại!");
            return "client/register";
        }
        customer.setNvkActive(true);
        nvkCustomerRepo.save(customer);
        return "redirect:/nvk-login?success=true";
    }

    // ==============================================================
    // 4. SẢN PHẨM CHI TIẾT
    // ==============================================================
    @GetMapping("/nvk-product/detail/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model) {
        nvkProduct product = productService.getProductById(id);
        if (product == null) return "redirect:/";
        model.addAttribute("nvkProduct", product);
        return "client/nvk-product-detail";
    }

    // ==============================================================
    // 5. GIỎ HÀNG + THANH TOÁN
    // ==============================================================
    @PostMapping("/nvk-cart/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam("quantity") int quantity,
                            jakarta.servlet.http.HttpServletRequest request,
                            HttpSession session) {
        if (session.getAttribute("nvkUserLogin") == null)
            return "redirect:/nvk-login?error=login_required_cart";

        cartService.addToCart(productId, quantity, session);
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
        return "redirect:/nvk-cart";
    }

    @PostMapping("/nvk-cart/checkout")
    public String checkout(@RequestParam("receiverName") String receiverName,
                           @RequestParam("receiverPhone") String receiverPhone,
                           @RequestParam("receiverAddress") String receiverAddress,
                           HttpSession session) {

        nvkCustomer currentUser = (nvkCustomer) session.getAttribute("nvkUserLogin");
        if (currentUser == null) return "redirect:/nvk-login";

        List<nvkCartItem> cart = cartService.getCart(session);
        if (cart.isEmpty()) return "redirect:/nvk-cart";

        // 1. Tạo đơn hàng
        nvkOrder order = new nvkOrder();
        order.setNvkCustomer(currentUser);
        order.setNvkCreatedDate(java.time.LocalDateTime.now());
        order.setNvkStatus(0); // 0: Chờ xác nhận
        order.setNvkTotalAmount(cartService.getTotalAmount(session));
        order.setNvkReceiverName(receiverName);
        order.setNvkReceiverPhone(receiverPhone);
        order.setNvkReceiverAddress(receiverAddress);
        order.setNvkCode("ORD-" + System.currentTimeMillis());

        nvkOrder savedOrder = orderRepo.save(order);

        // 2. Lưu chi tiết & Trừ kho
        for (nvkCartItem item : cart) {
            nvkOrderDetail detail = new nvkOrderDetail();
            detail.setNvkOrder(savedOrder);

            nvkProduct product = productService.getProductById(item.getProductId());

            detail.setNvkProduct(product);
            detail.setNvkQuantity(item.getQuantity());
            detail.setNvkPrice(item.getPrice());
            orderDetailRepo.save(detail);

            // --- TRỪ KHO ---
            if (product != null) {
                // Fix an toàn: Nếu null thì coi như 0 để tránh lỗi
                int currentStock = (product.getNvkQuantity() != null) ? product.getNvkQuantity() : 0;
                int buyQty = item.getQuantity();
                int newQty = currentStock - buyQty;

                if (newQty < 0) newQty = 0; // Chặn âm

                product.setNvkQuantity(newQty);

                // Nếu hết hàng -> Ẩn hoặc set trạng thái Hết hàng
                if (newQty == 0) {
                    product.setNvkStatus(0); // Hoặc 1 trạng thái khác tùy bro
                }

                productService.updateProduct(product);
            }
        }

        cartService.clearCart(session);
        return "redirect:/nvk-order/history?success=order_placed";
    }

    // ==============================================================
    // 6. HỒ SƠ NGƯỜI DÙNG
    // ==============================================================
    @GetMapping("/nvk-profile")
    public String profile(HttpSession session, Model model) {
        nvkCustomer currentUser = (nvkCustomer) session.getAttribute("nvkUserLogin");
        if (currentUser == null) return "redirect:/nvk-login";
        nvkCustomer freshUser = nvkCustomerRepo.findById(currentUser.getNvkId()).orElse(null);
        model.addAttribute("nvkCustomer", freshUser);
        return "client/nvk-profile";
    }

    @PostMapping("/nvk-profile/update")
    public String updateProfile(@ModelAttribute("nvkCustomer") nvkCustomer nvkCustomer,
                                @RequestParam("nvkAvatarFile") MultipartFile file,
                                HttpSession session) {
        if (!file.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get("src/main/resources/static/images/");
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
                try (InputStream is = file.getInputStream()) {
                    Files.copy(is, uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                }
                nvkCustomer.setNvkAvatar(fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        nvkCustomer updatedUser = nvkCustomerService.updateCustomer(nvkCustomer);
        if (updatedUser != null) session.setAttribute("nvkUserLogin", updatedUser);
        return "redirect:/nvk-profile?success=true";
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
    // 7. API AJAX (CART, REVIEW) - ĐÃ XÓA REGISTER RESTOCK
    // ==============================================================
    @PostMapping("/api/nvk-cart/add")
    @ResponseBody
    public java.util.Map<String, Object> addToCartAjax(@RequestParam("productId") Long productId,
                                                       @RequestParam("quantity") int quantity,
                                                       HttpSession session) {
        java.util.Map<String, Object> res = new java.util.HashMap<>();
        if (session.getAttribute("nvkUserLogin") == null) {
            res.put("status", "login_required");
            return res;
        }
        cartService.addToCart(productId, quantity, session);
        int newCount = cartService.getCart(session).stream().mapToInt(nvkCartItem::getQuantity).sum();
        res.put("status", "success");
        res.put("cartCount", newCount);
        return res;
    }

    @PostMapping("/api/nvk-review/submit")
    @ResponseBody
    public java.util.Map<String, Object> submitReviewAjax(@RequestParam("productId") Long productId,
                                                          @RequestParam("rating") Integer rating,
                                                          @RequestParam("reviewComment") String comment,
                                                          HttpSession session) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        nvkCustomer currentUser = (nvkCustomer) session.getAttribute("nvkUserLogin");
        if (currentUser == null) {
            response.put("status", "login_required");
            return response;
        }
        nvkProduct product = productService.getProductById(productId);
        if (product == null) {
            response.put("status", "error");
            response.put("message", "Sản phẩm không tồn tại!");
            return response;
        }
        nvkReview review = new nvkReview();
        review.setNvkProduct(product);
        review.setNvkCustomer(currentUser);
        review.setNvkRating(rating);
        review.setNvkComment(comment);
        review.setNvkCreatedDate(java.time.LocalDate.now());
        nvkReviewRepo.save(review);
        response.put("status", "success");
        response.put("userName", currentUser.getNvkFullName());
        response.put("userAvatar", currentUser.getNvkAvatar());
        response.put("rating", rating);
        response.put("comment", comment);
        response.put("date", java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy").format(java.time.LocalDate.now()));
        return response;
    }

    // ==============================================================
    // 8. LỊCH SỬ ĐƠN HÀNG
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
        if (order == null || !order.getNvkCustomer().getNvkId().equals(currentUser.getNvkId())) {
            return "redirect:/nvk-order/history";
        }
        List<nvkOrderDetail> details = orderDetailRepo.findByNvkOrder(order);
        model.addAttribute("nvkOrder", order);
        model.addAttribute("nvkOrderDetails", details);
        return "client/nvk-order-detail";
    }

    @GetMapping("/nvk-order/cancel/{id}")
    public String cancelOrder(@PathVariable("id") Long id, HttpSession session) {
        nvkCustomer currentUser = (nvkCustomer) session.getAttribute("nvkUserLogin");
        if (currentUser == null) return "redirect:/nvk-login";

        nvkOrder order = orderRepo.findById(id).orElse(null);

        if (order != null && order.getNvkCustomer().getNvkId().equals(currentUser.getNvkId())) {
            if (order.getNvkStatus() == 0) {
                order.setNvkStatus(4);
                orderRepo.save(order);

                // --- HOÀN KHO ---
                List<nvkOrderDetail> details = orderDetailRepo.findByNvkOrder(order);
                for (nvkOrderDetail detail : details) {
                    nvkProduct product = detail.getNvkProduct();

                    if (product != null) {
                        int qtyBought = detail.getNvkQuantity();
                        // Fix an toàn Null
                        int currentStock = (product.getNvkQuantity() != null) ? product.getNvkQuantity() : 0;

                        int newStock = currentStock + qtyBought;
                        product.setNvkQuantity(newStock);

                        if (product.getNvkStatus() == 0 && newStock > 0) {
                            product.setNvkStatus(1);
                        }
                        productService.updateProduct(product);
                    }
                }
            }
        }
        return "redirect:/nvk-order/history?success=cancelled";
    }

    // ==============================================================
    // 9. DANH MỤC & SẢN PHẨM
    // ==============================================================
    @GetMapping("/nvk-categories")
    public String nvkListCategories(Model model,
                                    @RequestParam(value = "categoryId", required = false) Long categoryId,
                                    @RequestParam(value = "sort", required = false, defaultValue = "default") String sort,
                                    @RequestParam(value = "inStock", required = false) Boolean inStock) {

        List<nvkCategory> categories = categoryService.getAllCategories();
        model.addAttribute("nvkCategories", categories);

        List<nvkProduct> products = productService.getAllProducts();

        if (categoryId != null) {
            products = products.stream()
                    .filter(p -> p.getNvkCategory() != null && p.getNvkCategory().getNvkId().equals(categoryId))
                    .collect(Collectors.toList());
        }

        if (inStock != null && inStock) {
            products = products.stream()
                    .filter(p -> p.getNvkQuantity() != null && p.getNvkQuantity() > 0 && p.getNvkStatus() == 1)
                    .collect(Collectors.toList());
        }

        // Sắp xếp
        if (sort != null) {
            switch (sort) {
                case "name-asc":
                    products.sort(Comparator.comparing(nvkProduct::getNvkName));
                    break;
                case "name-desc":
                    products.sort(Comparator.comparing(nvkProduct::getNvkName).reversed());
                    break;
                case "price-asc":
                    // Fix lỗi nếu giá null khi sắp xếp
                    products.sort(Comparator.comparing(p -> p.getNvkPrice() != null ? p.getNvkPrice() : 0.0));
                    break;
                case "price-desc":
                    products.sort(Comparator.comparing((nvkProduct p) -> p.getNvkPrice() != null ? p.getNvkPrice() : 0.0).reversed());
                    break;
                default:
                    products.sort(Comparator.comparing(nvkProduct::getNvkId).reversed());
                    break;
            }
        }

        model.addAttribute("nvkProducts", products);
        model.addAttribute("currentCategoryId", categoryId);
        model.addAttribute("currentSort", sort);
        model.addAttribute("currentInStock", inStock);

        return "nvk-category-list";
    }
}