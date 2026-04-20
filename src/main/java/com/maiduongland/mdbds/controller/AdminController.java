package com.maiduongland.mdbds.controller;

import com.maiduongland.mdbds.entity.Category;
import com.maiduongland.mdbds.entity.Post;
import com.maiduongland.mdbds.entity.Property;
import com.maiduongland.mdbds.service.PropertyService;
import com.maiduongland.mdbds.service.CategoryService;
import com.maiduongland.mdbds.service.PostService;
import com.maiduongland.mdbds.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class AdminController {

    private static final String ADMIN_KEY = "maiduong123";

    @Autowired
    private PropertyService propertyService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PostService postService;
    @Autowired
    private CloudinaryService cloudinaryService;

    private boolean isValidKey(String key) {
        return ADMIN_KEY.equals(key);
    }

    @GetMapping("/mdadmin-vung-dat-cam-6868")
    public String adminHome(@RequestParam(value = "key", required = false) String key, Model model) {
        if (!isValidKey(key)) return "error/403";
        try {
            model.addAttribute("propCount", propertyService.getAll().size());
            model.addAttribute("newsCount", postService.getAll().size());
        } catch (Exception e) {
            model.addAttribute("propCount", 0);
            model.addAttribute("newsCount", 0);
        }
        model.addAttribute("key", key);
        return "admin/index";
    }

    @GetMapping("/mdadmin-vung-dat-cam-6868/properties")
    public String dashboard(@RequestParam(value = "key", required = false) String key, Model model) {
        if (!isValidKey(key)) return "error/403";
        try {
            model.addAttribute("listProperties", propertyService.getAll());
        } catch (Exception e) {
            model.addAttribute("listProperties", java.util.Collections.emptyList());
        }
        model.addAttribute("key", key);
        return "admin/dashboard";
    }

    @GetMapping("/mdadmin-vung-dat-cam-6868/add")
    public String showAddForm(@RequestParam(value = "key", required = false) String key, Model model) {
        if (!isValidKey(key)) return "error/403";
        Property property = new Property();
        property.setCategory(new Category());
        model.addAttribute("property", property);
        try {
            model.addAttribute("listCategories", categoryService.getAll());
        } catch (Exception e) {
            model.addAttribute("listCategories", java.util.Collections.emptyList());
        }
        model.addAttribute("key", key);
        return "admin/form";
    }

    @GetMapping("/mdadmin-vung-dat-cam-6868/ping")
    @ResponseBody
    public String ping() {
        return "Admin Portal is LIVE - " + new java.util.Date();
    }

    @GetMapping({"/mdadmin-vung-dat-cam-6868/sua-thong-tin", "/mdadmin-vung-dat-cam-6868/sua-thong-tin/"})
    public String showEditForm(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "key", required = false) String key, Model model) {
        if (!isValidKey(key)) return "error/403";
        if (id == null) return "redirect:/mdadmin-vung-dat-cam-6868/properties?key=" + key;
        Property property = propertyService.getById(id);
        if (property == null) return "redirect:/mdadmin-vung-dat-cam-6868/properties?key=" + key;
        model.addAttribute("property", property);
        try {
            model.addAttribute("listCategories", categoryService.getAll());
        } catch (Exception e) {
            model.addAttribute("listCategories", java.util.Collections.emptyList());
        }
        model.addAttribute("key", key);
        return "admin/form";
    }

    @PostMapping("/mdadmin-vung-dat-cam-6868/save")
    public String save(@ModelAttribute Property property, 
                       @RequestParam(value = "imageFiles", required = false) MultipartFile[] imageFiles,
                       @RequestParam("key") String key, 
                       RedirectAttributes ra) throws IOException {
        if (!isValidKey(key)) return "error/403";

        if (imageFiles != null && imageFiles.length > 0 && !imageFiles[0].isEmpty()) {
            List<String> urls = cloudinaryService.uploadMultipleFiles(imageFiles);
            if (!urls.isEmpty()) {
                property.setImagesJson(String.join(",", urls));
                if (property.getThumbnailUrl() == null || property.getThumbnailUrl().isEmpty()) {
                    property.setThumbnailUrl(urls.get(0));
                }
            }
        }

        propertyService.save(property);
        ra.addAttribute("key", key);
        return "redirect:/mdadmin-vung-dat-cam-6868/properties";
    }

    @GetMapping("/mdadmin-vung-dat-cam-6868/delete/{id}")
    public String delete(@PathVariable("id") Long id, @RequestParam("key") String key, RedirectAttributes ra) {
        if (!isValidKey(key)) return "error/403";
        propertyService.delete(id);
        ra.addAttribute("key", key);
        return "redirect:/mdadmin-vung-dat-cam-6868/properties";
    }

    @GetMapping("/mdadmin-vung-dat-cam-6868/news")
    public String newsDashboard(@RequestParam(value = "key", required = false) String key, Model model) {
        if (!isValidKey(key)) return "error/403";
        try {
            model.addAttribute("listPosts", postService.getAll());
        } catch (Exception e) {
            model.addAttribute("listPosts", java.util.Collections.emptyList());
        }
        model.addAttribute("key", key);
        return "admin/news-dashboard";
    }

    @GetMapping("/mdadmin-vung-dat-cam-6868/news/add")
    public String showNewsForm(@RequestParam(value = "key", required = false) String key, Model model) {
        if (!isValidKey(key)) return "error/403";
        model.addAttribute("post", new Post());
        model.addAttribute("key", key);
        return "admin/news-form";
    }

    @GetMapping({"/mdadmin-vung-dat-cam-6868/news/sua-bai-viet", "/mdadmin-vung-dat-cam-6868/news/sua-bai-viet/"})
    public String showNewsEditForm(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "key", required = false) String key, Model model) {
        if (!isValidKey(key)) return "error/403";
        if (id == null) return "redirect:/mdadmin-vung-dat-cam-6868/news?key=" + key;
        Post post = postService.getById(id);
        if (post == null) return "redirect:/mdadmin-vung-dat-cam-6868/news?key=" + key;
        model.addAttribute("post", post);
        model.addAttribute("key", key);
        return "admin/news-form";
    }

    @PostMapping("/mdadmin-vung-dat-cam-6868/news/save")
    public String saveNews(@ModelAttribute Post post, 
                           @RequestParam(value = "imageFiles", required = false) MultipartFile[] imageFiles,
                           @RequestParam("key") String key, 
                           RedirectAttributes ra) throws IOException {
        if (!isValidKey(key)) return "error/403";

        if (imageFiles != null && imageFiles.length > 0 && !imageFiles[0].isEmpty()) {
            List<String> urls = cloudinaryService.uploadMultipleFiles(imageFiles);
            if (!urls.isEmpty()) {
                post.setImagesJson(String.join(",", urls));
                if (post.getThumbnailUrl() == null || post.getThumbnailUrl().isEmpty()) {
                    post.setThumbnailUrl(urls.get(0));
                }
            }
        }

        postService.save(post);
        ra.addAttribute("key", key);
        return "redirect:/mdadmin-vung-dat-cam-6868/news";
    }

    @GetMapping("/mdadmin-vung-dat-cam-6868/news/delete/{id}")
    public String deleteNews(@PathVariable("id") Long id, @RequestParam("key") String key, RedirectAttributes ra) {
        if (!isValidKey(key)) return "error/403";
        postService.delete(id);
        ra.addAttribute("key", key);
        return "redirect:/mdadmin-vung-dat-cam-6868/news";
    }
}
