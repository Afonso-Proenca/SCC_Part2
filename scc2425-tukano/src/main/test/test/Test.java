package test;

import tukano.api.Result;
import tukano.api.User;
import tukano.clients.rest.RestBlobsClient;
import tukano.clients.rest.RestShortsClient;
import tukano.clients.rest.RestUsersClient;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Random;

public class Test {
    
    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s");
    }
    
    public static void main(String[] args) throws Exception {
        // Setup
        Thread.sleep(1000);
        var serverURI = "http://127.0.0.1:8080/insert/rest/";
        var uriLogin = "http://127.0.0.1:8080/insert/rest/login";
        var blobEndpoint = "http://127.0.0.1:8080/insert/rest/blobs";

        var blobs = new RestBlobsClient(serverURI);
        var users = new RestUsersClient(serverURI);
        var shorts = new RestShortsClient(serverURI);

        // Login flow
        String sessionCookie = loginAndRetrieveCookie(uriLogin, "liskov", "54321");

        // User creation and blob interaction
        show(users.createUser(new User("liskov", "54321", "liskov@mit.edu", "Barbara Liskov")));

        Result<tukano.api.Short> s2 = shorts.createShort("liskov", "54321");
        show(s2);

        handleBlobOperations(blobs, s2, blobEndpoint, sessionCookie);
        
        System.exit(0);
    }

    private static Result<?> show(Result<?> res) {
        if (res.isOK()) {
            System.err.println("OK: " + res.value());
        } else {
            System.err.println("ERROR: " + res.error());
        }
        return res;
    }

    private static byte[] randomBytes(int size) {
        var r = new Random(1L);
        var bb = ByteBuffer.allocate(size);
        r.ints(size).forEach(i -> bb.put((byte) (i & 0xFF)));
        return bb.array();
    }

    private static String loginAndRetrieveCookie(String loginUri, String username, String password) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String form = String.format("username=%s&password=%s", username, password);
        HttpRequest loginRequest = HttpRequest.newBuilder()
                .uri(URI.create(loginUri))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();

        HttpResponse<String> loginResponse = client.send(loginRequest, HttpResponse.BodyHandlers.ofString());

        List<String> cookies = loginResponse.headers().allValues("Set-Cookie");
        if (cookies.isEmpty()) {
            throw new RuntimeException("No cookies received. Login failed.");
        }
        String sessionCookie = cookies.get(0).split(";")[0];
        System.out.println("Session Cookie: " + sessionCookie);
        return sessionCookie;
    }

    private static void handleBlobOperations(RestBlobsClient blobs, Result<tukano.api.Short> shortResult, String blobEndpoint, String sessionCookie) throws Exception {
        var blobUrl = URI.create(shortResult.value().getBlobUrl());
        System.out.println("Blob URL: " + blobUrl);

        var blobId = new File(blobUrl.getPath()).getName();
        System.out.println("Blob ID: " + blobId);

        var token = blobUrl.getQuery().split("=")[1];
        System.out.println("Token: " + token);

        uploadBlob(blobEndpoint, blobId, randomBytes(100), sessionCookie, token);
    }

    private static void uploadBlob(String blobEndpoint, String blobId, byte[] data, String sessionCookie, String token) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String uploadUrl = blobEndpoint + "/" + blobId + "?token=" + token;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uploadUrl))
                .header("Content-Type", "application/octet-stream")
                .header("Cookie", sessionCookie)
                .POST(HttpRequest.BodyPublishers.ofByteArray(data))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Upload Response: " + response.statusCode() + " - " + response.body());
    }
}

/*show(shorts.follow("liskov", "wales", true, "54321"));
		show(shorts.getFeed("liskov", "54321"));
		show(shorts.deleteShort(s1.value().id(), "12345"));
		show(shorts.getFeed("liskov", "54321"));
		show(shorts.follow("liskov", "wales", false, "54321"));
		show(shorts.getFeed("liskov", "54321"));
		show(shorts.follow("liskov", "wales", true, "54321"));
		show(shorts.createShort("wales", "12345"));
		show(shorts.createShort("wales", "12345"));
		show(shorts.createShort("wales", "12345"));
		show(shorts.createShort("wales", "12345"));
		show(shorts.getFeed("liskov", "54321"));
		/*show(shorts.follow("liskov", "wales", true, "54321"));
		show(shorts.followers("wales", "12345"));
		show(shorts.followers("wales", "12345"));
		show(shorts.follow("liskov", "wales", false, "54321"));
		show(shorts.followers("wales", "12345"));
		show(shorts.follow("liskov", "wales", true, "54321"));
		show(shorts.followers("wales", "12345"));
		show(shorts.followers("wales", "12345"));*/


		//show(shorts.getShort( s2id ));
		//show(shorts.getShort( s2id ));
		/*show(shorts.like(s2id, "liskov", true, "54321"));
		show(shorts.like(s2id, "liskov", true, "54321"));
		show(shorts.like(s2id, "wales", true, "12345"));
		show(shorts.getShort( s2id ));
		show(shorts.likes(s2id , "54321"));
		show(shorts.likes(s2id , "54321"));
		show(shorts.like(s2id, "liskov", false, "54321"));
		show(shorts.getShort( s2id ));
		show(shorts.likes(s2id , "54321"));
		show(shorts.like(s2id, "liskov", true, "54321"));
		show(shorts.likes(s2id , "54321"));
		show(shorts.likes(s2id , "54321"));*/
		//show(shorts.deleteShort(s2id, "54321"));
		//show(shorts.getFeed("liskov", "54321"));
		//show(shorts.getShort( s2id ));
		
		//show(shorts.getShorts( "wales" ));
		//show(shorts.getShorts( "wales" ));

		//show(shorts.followers("wales", "12345"));

		//show(shorts.getFeed("liskov", "12345"));

		//show(shorts.getShort( s2id ));
//
//		
//		blobs.forEach( b -> {
//			var r = b.download(blobId);
//			System.out.println( Hex.of(Hash.sha256( bytes )) + "-->" + Hex.of(Hash.sha256( r.value() )));
//			
//		});
		
		//show(users.deleteUser("wales", "12345"));
