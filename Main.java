import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        String username = "MrTechnodad";
        try {
            String baseURL = "https://www.reddit.com/user/"+username+"/submitted.json?limit=100";
            String userAgent = "RedditPostScraper [Credit to @AverageScar on Twitter - 2023]";
            String after = null;

            int consecutiveFailures = 0; // Track consecutive failures

            // Create a FileWriter and PrintWriter to write to the file
            FileWriter fileWriter = new FileWriter("posts.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);

            while (true) {
                // Construct the URL with the "after" parameter
                String url = baseURL + (after != null ? "&after=" + after : "");

                // Create an HTTP connection to the URL
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", userAgent);

                // Get the response code
                int responseCode = connection.getResponseCode();

                if (responseCode == 200) {
                    // Reading the response
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Parse the JSON response
                    JSONObject json = new JSONObject(response.toString());
                    JSONObject data = json.getJSONObject("data");
                    JSONArray children = data.getJSONArray("children");

                    for (int i = 0; i < children.length(); i++) {
                        JSONObject post = children.getJSONObject(i).getJSONObject("data");
                        String title = post.getString("title");
                        String text = post.getString("selftext");
                        if (Objects.equals(text, "")) {
                            text = "No Text Provided in Original Post.";
                        }
                        printWriter.println("Title: " + title.replaceAll("(&amp;#x200B;)+|(?<!\\\\)\\\\(?!\\\\)", ""));
                        printWriter.println("Text: " + text.replaceAll("(&amp;#x200B;)+|(?<!\\\\)\\\\(?!\\\\)", ""));
                        printWriter.println("--------------");

                        if (i == children.length() - 1) {
                            // Extract the "name" of the last post for the "after" parameter
                            after = post.getString("name");
                        }
                    }

                    // If "after" is null, there are no more posts
                    if (after == null) {
                        break; // Exit the loop
                    }

                    consecutiveFailures = 0; // Reset consecutive failures on success
                } else if (responseCode == 429) {
                    // Handle rate limiting (429 Too Many Requests)
                    printWriter.println("Rate limited. Waiting before the next request.");
                    // You can add a delay here and retry or exit the loop, depending on your needs.
                    Thread.sleep(5000);
                    // Increment consecutive failures
                    consecutiveFailures++;

                    if (consecutiveFailures >= 2) {
                        // If consecutive failures exceed a certain threshold, exit the loop
                        printWriter.println("Too many consecutive rate-limiting errors. Exiting.");
                        printWriter.close();
                        fileWriter.close();
                        System.exit(0);
                    }
                } else {
                    printWriter.println("HTTP GET request failed with response code: " + responseCode);
                    printWriter.close();
                    fileWriter.close();
                    System.exit(0); // Exit the loop on other errors
                }

                connection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
