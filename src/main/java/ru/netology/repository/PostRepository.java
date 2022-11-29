package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

// Stub
@Repository
public class PostRepository {
  private final CopyOnWriteArrayList<Post> posts = new CopyOnWriteArrayList<>();
  private AtomicInteger counterId = new AtomicInteger(0);

  private List<Post> getActualPosts() {
    return posts.stream().filter(it -> !it.isRemoved()).collect(Collectors.toList());
  }

  public List<Post> all() {
    return getActualPosts();
  }

  public Post getById(long id) throws NotFoundException {
    if (!posts.isEmpty()) {
      for (int i = 0; i < posts.size(); i++) {
        var curPost = posts.get(i);
        if (curPost.getId() == id) {
          if (curPost.isRemoved()) {
            throw new NotFoundException();
          }
          return Optional.of(curPost).get();
        }
      }
    }
    return (Post) Optional.empty().get();
  }

  public Post save(Post post) throws NotFoundException {
    int newPostId = 0;
    if (post.getId() == newPostId) {
      counterId.incrementAndGet();
      post.setId(counterId.get());
      posts.add(post);
    } else {
      for (var curPost: posts) {
        if (curPost.getId() == post.getId()) {
          if (curPost.isRemoved()) {
            throw new NotFoundException();
          }
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
    var actualPosts = getActualPosts();
    if (!actualPosts.isEmpty()) {
      for (int i = 0; i < actualPosts.size(); i++) {
        if (actualPosts.get(i).getId() == id) {
          actualPosts.get(i).setRemoved(true);
          return;
        }
      }
    }
  }
}
