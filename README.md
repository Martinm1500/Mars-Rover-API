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
      "name": "Tierra"
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
      // Additional maps...
    ]
    ```

**Note:**
- The response includes a list of all available maps.

  
## Interpreting the Map

The maps used in this API are represented as a grid with specified dimensions. Each cell in the grid can contain information about the terrain, obstacles, or the position of rovers. Here's a brief overview of the map representation:

- **Grid Dimensions:** The map has dimensions (dimensionX, dimensionY), where `dimensionX` represents the horizontal axis (left to right), and `dimensionY` represents the vertical axis (top to bottom).

- **Obstacles:** Obstacles on the map are represented as specific coordinates (x, y). When adding an obstacle to the map, provide the `x` and `y` coordinates where you want to place it.

- **Rovers:** Rovers are placed on the map with a specific position (x, y) and orientation (N, S, E, W for North, South, East, West, respectively).

- **Wrap Horizontally (Columns):** If a rover moves beyond the last column, it will reappear in the first column (and vice versa). This simulates the circular connection of the map columns.

- **Wrap Vertical (Rows):** If a rover moves beyond the first row to the north, it will remain in the first row but appear in the opposite column and its orientation will be updated to the south. The same applies if you move beyond the first row to the south.
Two opposite columns form a ring that surrounds the planet vertically.

- **Orientation changes:** If a rover crosses the North Pole or South Pole, its orientation should change to reflect the new position.
