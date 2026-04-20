package com.maiduongland.mdbds.controller;

import com.maiduongland.mdbds.service.PostService;
import com.maiduongland.mdbds.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PublicController {

    public PublicController() {
        System.out.println(">>> PublicController initialized - New mappings are active!");
    }

    @GetMapping("/test-connection")
    @ResponseBody
    public String testConnection() {
        return "System is reachable";
    }

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private PostService postService;

    @GetMapping("/")
    public String index(Model model) {
        try {
            model.addAttribute("listPosts", postService.getAll());
        } catch (Exception e) {
            model.addAttribute("listPosts", java.util.Collections.emptyList());
            System.err.println("Error fetching posts: " + e.getMessage());
        }
        return "index";
    }

    @GetMapping("/news/{slug}")
    public String newsDetail(@PathVariable String slug, Model model) {
        try {
            model.addAttribute("post", postService.getBySlug(slug));
        } catch (Exception e) {
            System.err.println("Error fetching post detail: " + e.getMessage());
        }
        return "news-detail";
    }

    @Autowired
    private com.maiduongland.mdbds.service.CategoryService categoryService;

    @GetMapping("/du-an")
    public String projects(@org.springframework.web.bind.annotation.RequestParam(value = "q", required = false) String q,
                           @org.springframework.web.bind.annotation.RequestParam(value = "cat", required = false) String catStr,
                           @org.springframework.web.bind.annotation.RequestParam(value = "min", required = false) String minStr,
                           @org.springframework.web.bind.annotation.RequestParam(value = "max", required = false) String maxStr,
                           Model model) {
        try {
            Long cat = (catStr != null && !catStr.isEmpty()) ? Long.parseLong(catStr) : null;
            Double min = (minStr != null && !minStr.isEmpty()) ? Double.parseDouble(minStr) : null;
            Double max = (maxStr != null && !maxStr.isEmpty()) ? Double.parseDouble(maxStr) : null;

            // Get Featured (Hot) Properties - always show latest 5
            model.addAttribute("hotProperties", propertyService.getHotPropertiesLimit5());
            
            // Get filtered search results
            model.addAttribute("listProperties", propertyService.searchProperties(q, cat, min, max));
            
            // Hardcoded Fallback Categories if DB is empty
            java.util.List<com.maiduongland.mdbds.entity.Category> cats = categoryService.getAll();
            if (cats == null || cats.isEmpty()) {
                cats = new java.util.ArrayList<>();
                cats.add(new com.maiduongland.mdbds.entity.Category(1L, "Đất nền", "dat-nen", null));
                cats.add(new com.maiduongland.mdbds.entity.Category(2L, "Nhà ở", "nha-o", null));
                cats.add(new com.maiduongland.mdbds.entity.Category(3L, "Chung cư", "chung-cu", null));
                cats.add(new com.maiduongland.mdbds.entity.Category(4L, "Dự án", "du-an", null));
            }
            model.addAttribute("listCategories", cats);
            
            // Keep search params in model for UI
            model.addAttribute("q", q);
            model.addAttribute("catId", cat);
            model.addAttribute("minPrice", min);
            model.addAttribute("maxPrice", max);
            
        } catch (Exception e) {
            model.addAttribute("hotProperties", java.util.Collections.emptyList());
            model.addAttribute("listProperties", java.util.Collections.emptyList());
            model.addAttribute("listCategories", java.util.Collections.emptyList());
            System.err.println("Error fetching projects: " + e.getMessage());
        }
        return "projects";
    }

    @GetMapping("/property/{slug}")
    public String propertyDetail(@PathVariable String slug, Model model) {
        try {
            model.addAttribute("p", propertyService.getBySlug(slug));
        } catch (Exception e) {
            System.err.println("Error fetching property detail: " + e.getMessage());
        }
        return "property-detail";
    }

    @GetMapping("/chinh-sach/bao-mat")
    public String privacyPolicy() {
        try {
            return "policy/privacy";
        } catch (Exception e) {
            System.err.println("Error rendering privacy policy: " + e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/chinh-sach/dieu-khoan")
    public String termsOfService() {
        try {
            return "policy/terms";
        } catch (Exception e) {
            System.err.println("Error rendering terms of service: " + e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/chinh-sach/quy-trinh")
    public String transactionProcess() {
        try {
            return "policy/process";
        } catch (Exception e) {
            System.err.println("Error rendering transaction process: " + e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/gioi-thieu")
    public String about() {
        return "gioi-thieu";
    }
}
