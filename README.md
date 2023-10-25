# Reddit User Scraper

Reddit User Scraper is a simple Java program that allows you to fetch and save posts from a Reddit user's submission history.

## Features

- Fetch Reddit posts from a user's submission history.
- Save the fetched posts to a text file.
- Gracefully handle rate limiting (HTTP 429 responses).
- Automatically exit the program when the end of the list is reached.

## Prerequisites

- Java Development Kit (JDK) installed on your system.
- IDE (Optional, but advised)
- Gradle

## Usage

1) Modify the username variable in src/Main.java
2) Build the JAR (Gradle not included in this code)
3) Run
4) Look for a file called posts.txt 

The program will fetch and save the posts in batches of 100, handle rate limiting, and automatically exit when the end of the list is reached.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Author

- [AverageScar](https://twitter.com/@averagescar)

## Acknowledgments

- This program uses the Reddit API to fetch posts.
