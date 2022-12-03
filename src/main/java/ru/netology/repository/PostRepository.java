package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

// Stub
@Repository
public class PostRepository {
    private final ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong counterId = new AtomicLong(0);

    private List<Post> getActualPosts() {
        return posts.values().stream().filter(it -> !it.isRemoved()).collect(Collectors.toList());
    }

    public List<Post> all() {
        return getActualPosts();
    }

    public Optional<Post> getById(long id) throws NotFoundException {
        if (!posts.isEmpty() && posts.containsKey(id)) {
            Post foundPost = posts.get(id);
            if (foundPost.isRemoved()) {
                throw new NotFoundException();
            } else {
                return Optional.of(foundPost);
            }
        }
        return Optional.empty();
    }

    public Post save(Post post) throws NotFoundException {
        int newPostId = 0;
        if (post.getId() == newPostId) {
            post.setId(counterId.incrementAndGet());
            posts.put(post.getId(), post);
        } else {
            Post postToUpdate = posts.get(post.getId());
            if (postToUpdate != null) {
                if (postToUpdate.isRemoved()) {
                    throw new NotFoundException();
                } else {
                    posts.replace(post.getId(), post);
                }
            } else {
                post.setId(counterId.incrementAndGet());
                posts.put(post.getId(), post);
            }
        }
        return post;
    }

    public void removeById(long id) {
        List<Post> actualPosts = getActualPosts();
        if (!actualPosts.isEmpty()) {
            actualPosts.stream()
                .filter(it -> it.getId() == id)
                .collect(Collectors.toList())
                .get(0)
                .setRemoved(true);
        }
    }
}
