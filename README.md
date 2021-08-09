# Simple Release Tracker Service



### How to run the project

##### Clone source code from git
```
$ git clone https://github.com/justanothernerdhere/simplereleasetracker.git
```

##### Build Docker image
```
$ docker-compose build && docker-compose up
```
This will run the docker-compose.yml file which does the following:

- Builds the project in a container
- Sets up postgres db
- Starts and runs the application on port 9000

##### Test application
```
$ curl localhost:9000/api/releases/1
```
Reponse:
```json
{
    "id": 1,
    "name": "release_0",
    "description": "This is my AWESOME release number 0",
    "status": "On PROD",
    "releaseDate": "2021-12-04",
    "createdAtDate": "2021-08-08",
    "lastUpdateAtDate": "2021-08-08"
}
```

## GET /api/releases `200 OK`
```json
[
    {
        "id": 1,
        "name": "release_0",
        "description": "This is my AWESOME release number 0",
        "status": "On PROD",
        "releaseDate": "2022-02-20",
        "createdAtDate": "2021-08-08",
        "lastUpdateAtDate": "2021-08-08"
    }
]
```
- **Filtering:**
  to narrow down the query results by specific parameters, eg. creation date, or status of release
```
GET /api/releases?releaseDateBefore=2021-09-01
GET /api/releases?releaseDateGt=2021-12-01&createdAtDateLt=2021-10-01
```

- **Sorting:**
  Sorting also enabled, to help organize the results for larger datasets:
```
GET /api/releases?sort=name,desc
GET /api/releases?sort=releaseDate,asc
```

- **Paging:**  
  This api also has paging, which allows you to limit and page out the number of results. This is of course useful for larger datasets.
```
GET /api/releases?limit=10&offset=0
```

A more complex query may be as follows:
```
GET /api/releases?state=Complete&sort=name,desc&limit=1&offset=0
```

## POST /api/releases `201 created`
You may create a new release using a post mapping on the api. You must have a body with the following required fields, lest you encur a `400 - Request body is malformed` resp.
#### body:
```json
{
    "name": "release_custom",
    "description": "some custom release",
    "status": "Created",
    "releaseDate": "2021-08-06"
}
```
### response:
```json
{
    "id": 1004,
    "name": "release_2",
    "description": "the first release",
    "status": "Created",
    "releaseDate": "2021-08-06",
    "createdAtDate": "2021-08-08",
    "lastUpdateAtDate": "2021-08-08"
}
```

## PUT /api/releases `201 created`
You may update an existing release using the put mapping on the api. You must supply the entire object with all valid fields (columns) to be updated, lest you encur a `400 - Request body is malformed` resp.
#### body:
```json
{
    "id":1,
    "name":"EPIC release ",
    "description": "Not just an awesome release, an EPIC release",
    "status": "QA done on STAGING",
    "releaseDate": "2022-08-06",
    "lastUpdateAtDate": "2021-08-07",
    "createdAtDate":"2022-07-09"
}
```
### response:
```json
{
    "id": 1,
    "name": "EPIC",
    "description": "This is release number 0",
    "status": "QA done on STAGING",
    "releaseDate": "2022-08-06",
    "createdAtDate": "2022-07-09",
    "lastUpdateAtDate": "2021-08-07"
}
```
## DELETE /api/releases/{id} `204 No Content`
You may delete a release with a delete mapping on the api. An invalid or unfound release id will result in `404 - No releases found with specified id`

## Testing Application
This application contains a few basic tests to explemplify testing standards on a simple api. More complex tests could be written for example to test filtering and sorting, as well as more unique exeption handling on edge cases.

```
public void get_should_throw_EntityNotFoundException()
public void get_should_return_the_entity()
public void get_by_specification_should_return_entities_list()
public void get_by_specification_and_pagination_should_return_paged_entities_list()
public void get_by_specification_and_sort_should_return_entities_list()
public void create_should_return_saved_entity()
public void update_should_return_updated_entity()
public void save_should_throws_UpdateIdMismatchException()
public void save_should_throws_Exception()
public void delete()
```

