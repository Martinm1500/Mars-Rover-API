# Rover API Documentation

The Rover API enables interaction with rovers and maps, providing basic operations through specific interfaces.

## Endpoints - Maps

### Create a Map

- **Endpoint:** `POST /api/maps/create`
- **Description:** Creates a new map.
- **Request Body:**
  ```json
  {
    "dimensionX": 8,
    "dimensionY": 8,
    "name": "Tierra"
  }
  ```
- **Responses:**
  - **200 OK:** Returns the created map.
    ```json
    {
      "id": 1,
      "dimensionX": 8,
      "dimensionY": 8,
      "name": "Tierra"
    }
    ```
  - **400 Bad Request:** If the provided map dimensions are invalid.
    ```json
    {
      "error": "InvalidMapDimensionsException",
      "message": "Invalid map dimensions. Please provide valid dimensions."
    }
    ```

**Note:**
- Ensure the request payload includes the required map dimensions (`dimensionX` and `dimensionY`).
- The response includes the created map details if successful.
- If the map creation fails due to invalid dimensions, a `Bad Request` response is returned along with an error message indicating the cause of the failure.

### Delete a Map

- **Endpoint:** `DELETE /api/maps/delete/{id}`
- **Description:** Deletes an existing map by its ID.
- **URL Parameters:**
  - `id`: The unique identifier of the map to be deleted.
- **Responses:**
  - **200 OK:** Returns a success message upon successful deletion.
    ```json
    {
      "message": "Map with ID: 1 was deleted successfully"
    }
    ```
  - **404 Not Found:** If the specified map ID is not found.
    ```json
    {
      "error": "MapNotFoundException",
      "message": "Map with ID: 1 not found"
    }
    ```

**Note:**
- Ensure the `id` parameter is provided in the endpoint URL.
- The response includes a success message if the deletion is successful.
- If the specified map ID is not found, a `Not Found` response is returned along with an error message.

### Get Map Details

- **Endpoint:** `GET /api/maps/get/{id}`
- **Description:** Retrieves details of a map based on its ID.
- **URL Parameters:**
  - `id`: The unique identifier of the map to be retrieved.
- **Responses:**
  - **200 OK:** Returns the details of the specified map.
    ```json
    {
      "id": 1,
      "dimensionX": 8,
      "dimensionY": 8,
      "name": "Tierra",
      "obstacles": [
        {
          "x": 1,
          "y": 4
        },
        {
          "x": 3,
          "y": 6
        }
      ],
      "rover":
      {
        "id": 1,
        "x": 3,
        "y": 3,
        "orientation": "N",
        "mapId": 1
      }
    }
    ```
  - **404 Not Found:** If the specified map ID is not found.
    ```json
    {
      "error": "MapNotFoundException",
      "message": "Map with ID: 1 not found"
    }
    ```

**Note:**
- Ensure the `id` parameter is provided in the endpoint URL.
- The response includes the details of the specified map if it exists.
- If the specified map ID is not found, a `Not Found` response is returned along with an error message.

### Get All Maps

- **Endpoint:** `GET /api/maps/get/all`
- **Description:** Retrieves details of all available maps.
- **Responses:**
  - **200 OK:** Returns a list of all maps.
    ```json
    [
      {
        "id": 1,
        "dimensionX": 8,
        "dimensionY": 8,
        "name": "Tierra"
      },
    ]
    ```

**Note:**
- The response includes a list of all available maps.


## Endpoints - Rovers

# Create Rover on Map

- **Endpoint:** `POST /api/rovers/create-on-map/{mapId}`
- **Description:** Creates a new rover on a specified map.
- **Request:**
  - **URL Parameters:**
    - `mapId`: The unique identifier of the map where the rover should be created.
  - **Method:** POST
  - **Content-Type:** application/json
  - **Body:**
    ```json
    {
      "x": 3,
      "y": 4,
      "orientation": "N"
    }
    ```
- **Responses:**
  - **201 Created:** Returns the details of the created rover.
    ```json
    {
      "id": 1,
      "x": 3,
      "y": 4,
      "orientation": "N",
      "mapId": 1
    }
    ```
  - **404 Not Found:** If the specified map ID is not found.
    ```json
    {
      "error": "MapNotFoundException",
      "message": "Map with ID: 1 not found"
    }
    ```
  - **400 Bad Request:**
    - If the provided coordinates or orientation are invalid.
    - If an attempt is made to create a rover on a map that already has a rover.
    ```json
    {
      "error": "InvalidCoordinatesException | InvalidOrientationException | InvalidOperationException",
      "message": "Details about the specific error"
    }
    ```

**Note:**
- Ensure the `mapId` parameter is provided in the endpoint URL.
- The request payload should include the initial coordinates (`x` and `y`) and orientation of the rover.
- The response includes the details of the created rover if successful.
- If the specified map ID is not found or there is an issue with the provided rover details, appropriate error responses are returned.

# Delete Rover

- **Endpoint:** `DELETE /api/rovers/delete/{roverId}`
- **Description:** Deletes an existing rover by its ID.
- **URL Parameters:**
  - `roverId`: The unique identifier of the rover to be deleted.
- **Responses:**
  - **200 OK:** Returns a success message upon successful deletion.
    ```json
    {
      "message": "Rover with ID: 1 was deleted successfully"
    }
    ```
  - **404 Not Found:** If the specified rover ID is not found.
    ```json
    {
      "error": "RoverNotFoundException",
      "message": "Rover with ID: 1 not found"
    }
    ```

**Note:**
- Ensure the `roverId` parameter is provided in the endpoint URL.
- The response includes a success message if the deletion is successful.
- If the specified rover ID is not found, a `Not Found` response is returned along with an error message.

# Update Rover

- **Endpoint:** `PUT /api/rovers/update`
- **Description:** Updates the details of an existing rover.
- **Request:**
  - **Method:** PUT
  - **Content-Type:** application/json
  - **Body:**
    ```json
    {
      "id": 1,
      "x": 5,
      "y": 6,
      "orientation": "EAST",
      "mapId":1
    }
    ```
- **Responses:**
  - **200 OK:** Returns the details of the updated rover.
    ```json
    {
      "id": 1,
      "x": 5,
      "y": 6,
      "orientation": "EAST",
      "mapId":1
    }
    ```
  - **404 Not Found:** If the specified rover ID is not found.
    ```json
    {
      "error": "RoverNotFoundException",
      "message": "Rover with ID: 1 not found"
    }
    ```
  - **400 Bad Request:**
    - If the provided coordinates or orientation are invalid.
    ```json
    {
      "error": "InvalidCoordinatesException | InvalidOrientationException",
      "message": "Details about the specific error"
    }
    ```

**Note:**
- The request payload should include the updated coordinates (`x` and `y`) and orientation of the rover.
- The response includes the details of the updated rover if successful.
- If the specified rover ID is not found or there is an issue with the provided rover details, appropriate error responses are returned.

# Get Rover Details

- **Endpoint:** `GET /api/rovers/{roverId}`
- **Description:** Retrieves details of a specific rover by its ID.
- **URL Parameters:**
  - `roverId`: The unique identifier of the rover to be retrieved.
- **Responses:**
  - **200 OK:** Returns the details of the specified rover.
    ```json
    {
      "id": 1,
      "x": 5,
      "y": 6,
      "orientation": "EAST"
    }
    ```
  - **404 Not Found:** If the specified rover ID is not found.
    ```json
    {
      "error": "RoverNotFoundException",
      "message": "Rover with ID: 1 not found"
    }
    ```

**Note:**
- Ensure the `roverId` parameter is provided in the endpoint URL.
- The response includes the details of the specified rover if it exists.
- If the specified rover ID is not found, a `Not Found` response is returned along with an error message.

# Get Rover by Map ID

- **Endpoint:** `GET /api/rovers/by-map/{mapId}`
- **Description:** Retrieves details of a rover on a specified map.
- **URL Parameters:**
  - `mapId`: The unique identifier of the map for which the rover details are requested.
- **Responses:**
  - **200 OK:** Returns the details of the rover on the specified map.
    ```json
    {
      "id": 1,
      "x": 5,
      "y": 6,
      "orientation": "EAST",
      "mapId": 1
    }
    ```
  - **404 Not Found:** If the specified map ID is not found.
    ```json
    {
      "error": "MapNotFoundException",
      "message": "Map with ID: 1 not found"
    }
    ```

**Note:**
- Ensure the `mapId` parameter is provided in the endpoint URL.
- The response includes the details of the rover on the specified map if it exists.
- If the specified map ID is not found, a `Not Found` response is returned along with an error message.

# Execute Commands on Rover

- **Endpoint:** `POST /api/rovers/execute-commands/{roverId}`
- **Description:** Executes a series of commands on a specified rover.
- **URL Parameters:**
  - `roverId`: The unique identifier of the rover on which commands are to be executed.
- **Request:**
  - **Method:** POST
  - **Content-Type:** application/json
  - **Body:**
    ```json
    ["l", "l", "f", "f", "r"]
    ```
- **Responses:**
  - **200 OK:** Commands executed successfully.
  - **404 Not Found:** If the specified rover ID is not found.
    ```json
    {
      "error": "RoverNotFoundException",
      "message": "Rover with ID: 1 not found"
    }
    ```
  - **409 Conflict:** If the mission is aborted due to an obstacle.
    ```json
    {
      "error": "MissionAbortedException",
      "message": "Mission aborted due to obstacle at coordinates: (3, 4)"
    }
    ```
  - **400 Bad Request:** If the provided commands are invalid.
    ```json
    {
      "error": "InvalidCommandException",
      "message": "Invalid command. Please provide valid commands."
    }
    ```

**Note:**
- Ensure the `roverId` parameter is provided in the endpoint URL.
- The request payload should include a list of commands to be executed (`["L", "M", "R"]`).
- If the specified rover ID is not found or there is an issue with the provided commands, appropriate error responses are returned.
- If the mission is aborted due to an obstacle, a `Conflict` response is returned along with an error message indicating the obstacle's coordinates.

## Endpoints - Obstacles

# Create Obstacle on Map

- **Endpoint:** `POST /api/obstacles/create-on-map/{mapId}`
- **Description:** Creates a new obstacle on a specified map.
- **Request:**
  - **Method:** POST
  - **Content-Type:** application/json
  - **Body:**
    ```json
    {
      "x": 3,
      "y": 4
    }
    ```
- **URL Parameters:**
  - `mapId`: The unique identifier of the map where the obstacle should be created.
- **Responses:**
  - **201 Created:** Returns the details of the created obstacle.
    ```json
    {
      "x": 3,
      "y": 4,
    }
    ```
  - **404 Not Found:** If the specified map ID is not found.
    ```json
    {
      "error": "MapNotFoundException",
      "message": "Map with ID: 1 not found"
    }
    ```
  - **400 Bad Request:**
    - If the provided coordinates for the obstacle are invalid.
    ```json
    {
      "error": "InvalidCoordinatesException",
      "message": "Invalid coordinates for the obstacle. Please provide valid coordinates."
    }
    ```

**Note:**
- Ensure the `mapId` parameter is provided in the endpoint URL.
- The request payload should include the coordinates (`x` and `y`) of the obstacle.
- The response includes the details of the created obstacle if successful.
- If the specified map ID is not found or there is an issue with the provided obstacle details, appropriate error responses are returned.

# Delete Obstacle

- **Endpoint:** `DELETE /api/obstacles/delete/{obstacleId}`
- **Description:** Deletes an existing obstacle by its ID.
- **URL Parameters:**
  - `obstacleId`: The unique identifier of the obstacle to be deleted.
- **Responses:**
  - **200 OK:** Returns a success message upon successful deletion.
    ```json
    {
      "message": "Obstacle with ID: 1 was deleted successfully"
    }
    ```
  - **404 Not Found:** If the specified obstacle ID is not found.
    ```json
    {
      "error": "ObstacleNotFoundException",
      "message": "Obstacle with ID: 1 not found"
    }
    ```

**Note:**
- Ensure the `obstacleId` parameter is provided in the endpoint URL.
- The response includes a success message if the deletion is successful.
- If the specified obstacle ID is not found, a `Not Found` response is returned along with an error message.

# Get Obstacle Details

- **Endpoint:** `GET /api/obstacles/get/{obstacleId}`
- **Description:** Retrieves details of a specific obstacle by its ID.
- **URL Parameters:**
  - `obstacleId`: The unique identifier of the obstacle to be retrieved.
- **Responses:**
  - **200 OK:** Returns the details of the specified obstacle.
    ```json
    {
      "x": 3,
      "y": 4,
    }
    ```
  - **404 Not Found:** If the specified obstacle ID is not found.
    ```json
    {
      "error": "ObstacleNotFoundException",
      "message": "Obstacle with ID: 1 not found"
    }
    ```

**Note:**
- Ensure the `obstacleId` parameter is provided in the endpoint URL.
- The response includes the details of the specified obstacle if it exists.
- If the specified obstacle ID is not found, a `Not Found` response is returned along with an error message.

# Get All Obstacles from Map

- **Endpoint:** `GET /api/obstacles/get/all-from-map/{mapId}`
- **Description:** Retrieves details of all obstacles from a specified map.
- **URL Parameters:**
  - `mapId`: The unique identifier of the map for which obstacle details are requested.
- **Responses:**
  - **200 OK:** Returns a list of obstacles from the specified map.
    ```json
    [
      {
        "x": 3,
        "y": 4,
      },
      // Additional obstacles...
    ]
    ```
  - **404 Not Found:** If the specified map ID is not found.
    ```json
    {
      "error": "MapNotFoundException",
      "message": "Map with ID: 1 not found"
    }
    ```

**Note:**
- Ensure the `mapId` parameter is provided in the endpoint URL.
- The response includes a list of obstacles from the specified map if they exist.
- If the specified map ID is not found, a `Not Found` response is returned along with an error message.

## Interpreting the Map

The maps used in this API are represented as a grid with specified dimensions. Each cell in the grid can contain information about the terrain, obstacles, or the position of rovers. Here's a brief overview of the map representation:

- **Grid Dimensions:** The map has dimensions (dimensionX, dimensionY), where `dimensionX` represents the horizontal axis (left to right), and `dimensionY` represents the vertical axis (top to bottom).

- **Obstacles:** Obstacles on the map are represented as specific coordinates (x, y). When adding an obstacle to the map, provide the `x` and `y` coordinates where you want to place it.

- **Rovers:** Rovers are placed on the map with a specific position (x, y) and orientation (N, S, E, W for North, South, East, West, respectively).

- **Wrap Horizontally (Columns):** If a rover moves beyond the last column, it will reappear in the first column (and vice versa). This simulates the circular connection of the map columns.

- **Wrap Vertical (Rows):** If a rover moves beyond the first row to the north, it will remain in the first row but appear in the opposite column and its orientation will be updated to the south. The same applies if you move beyond the first row to the south.
Two opposite columns form a ring that surrounds the planet vertically.

- **Orientation changes:** If a rover crosses the North Pole or South Pole, its orientation should change to reflect the new position.
