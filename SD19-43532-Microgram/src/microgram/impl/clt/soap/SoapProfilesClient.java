package microgram.impl.clt.soap;

import java.net.URI;
import java.util.List;

import microgram.api.Profile;
import microgram.api.java.Profiles;
import microgram.api.java.Result;
import microgram.api.soap.SoapProfiles;

//TODO Make this class concrete
public class SoapProfilesClient extends SoapClient implements Profiles {

	SoapProfiles impl;

	public SoapProfilesClient(URI serverUri) {
		super(serverUri);
	}

	private SoapProfiles impl() {
		if( impl == null ) {
			//TODO			
		}
		return impl;
	}

	@Override
	public Result<Profile> getProfile(String userId) {
		return super.tryCatchResult(() -> impl().getProfile(userId));
	}

	@Override
	public Result<Void> createProfile(Profile profile) {
		return super.tryCatchVoid(() -> impl().createProfile(profile));
	}

	@Override
	public Result<Void> deleteProfile(String userId) {
		return super.tryCatchVoid(() -> impl().deleteProfile(userId));
	}

	@Override
	public Result<List<Profile>> search(String prefix) {
		return super.tryCatchResult(() -> impl().search(prefix));
	}

	@Override
	public Result<Void> follow(String userId1, String userId2, boolean isFollowing) {
		return super.tryCatchVoid(() -> impl().follow(userId1, userId2, isFollowing));
	}

	@Override
	public Result<Boolean> isFollowing(String userId1, String userId2) {
		return super.tryCatchResult(() -> impl().isFollowing(userId1, userId2));
	}

	@Override
	public Result<List<String>> listOfFollowings(String userId) {
		return super.tryCatchResult(() -> impl().listOfFollowings(userId));
	}

}
