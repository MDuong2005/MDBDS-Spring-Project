package com.maiduongland.mdbds.service;

import com.maiduongland.mdbds.entity.Post;
import com.maiduongland.mdbds.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository repository;

    public List<Post> getAll() {
        return repository.findAll();
    }

    public Post getBySlug(String slug) {
        return repository.findBySlug(slug).orElse(null);
    }

    public Post save(Post post) {
        return repository.save(post);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Post getById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
