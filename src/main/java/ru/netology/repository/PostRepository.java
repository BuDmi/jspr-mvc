package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

// Stub
@Repository
public class PostRepository {
  private final CopyOnWriteArrayList<Post> posts = new CopyOnWriteArrayList<>();
  private AtomicInteger counterId = new AtomicInteger(0);
  public List<Post> all() {
    return posts;
  }

  public Optional<Post> getById(long id) {
    if (!posts.isEmpty()) {
      for (int i = 0; i < posts.size(); i++) {
        var curPost = posts.get(i);
        if (curPost.getId() == id) {
          return Optional.of(curPost);
        }
      }
    }
    return Optional.empty();
  }

  public Post save(Post post) {
    int newPostId = 0;
    if (post.getId() == newPostId) {
      counterId.incrementAndGet();
      post.setId(counterId.get());
      posts.add(post);
    } else {
      for (var curPost: posts) {
        if (curPost.getId() == post.getId()) {
          curPost.setContent(post.getContent());
          return post;
        }
      }
      post.setId(posts.size());
      posts.add(post);
    }
    return post;
  }

  public void removeById(long id) {
    if (!posts.isEmpty()) {
      for (int i = 0; i < posts.size(); i++) {
        if (posts.get(i).getId() == id) {
          posts.remove(i);
          return;
        }
      }
    }  }
}
