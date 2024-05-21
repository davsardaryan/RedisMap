# RedisMap

Redis Map is a Java class that provides a convenient interface for working with a Redis data structure as if it were a Java Map.

## Features

- Provides a simple way to interact with a Redis data structure.
- Allows you to use familiar Map methods.
- Designed to work with Redis clusters for scalability and high availability.

## Installation

- The lib directory contains all the necessary libraries.
- Ensure that your Redis server is running.
- It works on default host and port (Jedis jedis = new Jedis("localhost", 6379))
- In your case maybe you need to change host and port