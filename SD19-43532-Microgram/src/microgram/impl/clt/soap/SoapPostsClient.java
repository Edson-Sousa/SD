package microgram.impl.clt.soap;

import java.net.URI;
import java.util.List;

import microgram.api.Post;
import microgram.api.java.Posts;
import microgram.api.java.Result;
import microgram.api.soap.SoapPosts;

public class SoapPostsClient extends SoapClient implements Posts {

	SoapPosts impl;
	
	public SoapPostsClient(URI serverUri) {
		super(serverUri);
	}

	private SoapPosts impl() {
		if( impl == null ) {
			//TODO
		}
		return impl;
	}
	
	
	@Override
	public Result<Post> getPost(String postId) {
		return super.tryCatchResult(() -> impl().getPost(postId));
	}
	
	@Override
	public Result<String> createPost(Post post) {
		return super.tryCatchResult(() -> impl().createPost(post));
	}

	@Override
	public Result<Void> deletePost(String postId) {
		return super.tryCatchVoid(() -> impl().deletePost(postId));
	}

	@Override
	public Result<Void> like(String postId, String userId, boolean isLiked) {
		return super.tryCatchVoid(() -> impl().like(postId, userId, isLiked));
	}

	@Override
	public Result<Boolean> isLiked(String postId, String userId) {
		return super.tryCatchResult(() -> impl().isLiked(postId, userId));
	}

	@Override
	public Result<List<String>> getPosts(String userId) {
		return super.tryCatchResult(() -> impl().getPosts(userId));
	}

	@Override
	public Result<List<String>> getFeed(String userId) {
		return super.tryCatchResult(() -> impl().getFeed(userId));
	}
}
