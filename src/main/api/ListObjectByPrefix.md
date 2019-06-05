**List object by prefix**
----
    Lists the name of stored document whit name starting whit specified prefix.
* **URL**

  /list/:prefix

* **Method:**

  `GET`

*  **URL Params**

   **Required:**

   `prefix = [string]`

   `prefix` is pattern to search at the beginning of filenames


* **Data Params**

    None

* **Success Response:**

  * **Code:** 200 OK <br />
    **Content:** `[
        "Temp1.xml",
        "Temp2.txt"
    ]`
