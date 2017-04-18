# difftool
DiffTool App built in Spring Boot + H2 DB

Web Application built in Spring Boot for Diffing Json Base64 encoded text data.

Presentation of the tool itself: [Link](https://goo.gl/sxY7NQ)

Tools used:
* Java JDK 1.8
* H2 Database
* Eclipse JEE (IDE)
* Apache Maven (dependency management)
* POSTMAN (functional tests)

REST interfaces for usage:
* POST: /v1/diff/ID/left

  Request:
```
"{\"hello\": \"world\"}"
```

  Response: 
```
{
  "id": 2,
  "left": "{\"hello\": \"world\"}",
  "right": null,
  "empty": false,
  "equal": true,
  "complete": true,
  "sameSize": true,
  "lengthRight": 18,
  "lengthLeft": 18
}

```

* POST: /v1/diff/ID/right

  * Request:
```
"{\"hello\": \"world\"}"
```

  * Response: 
```
{
  "id": 2,
  "left": "{\"hello\": \"world\"}",
  "right": "{\"hello\": \"world\"}",
  "empty": false,
  "equal": true,
  "complete": true,
  "sameSize": true,
  "lengthRight": 18,
  "lengthLeft": 18
}

```
* GET: v1/diff/ID

  * Equal sides:
```
{
  "id": 2,
  "occurrences": null,
  "equal": true
}
```

  * Different sides: 
```
{
  "id": 2,
  "occurrences": null,
  "equal": false
}
```

  * Equal size, different data:
 ```
{
  "id": 4,
  "occurrences": [
    {
      "position": 41,
      "size": 1
    }
  ],
  "equal": false
} 
 ```

* GET: /v1/diff/ID/doc
```
{
  "id": 2,
  "left": "{\"hello\": \"world\"}",
  "right": "{\"hello\": \"world\"}",
  "empty": false,
  "equal": true,
  "complete": true,
  "sameSize": true,
  "lengthRight": 18,
  "lengthLeft": 18
} 
```
