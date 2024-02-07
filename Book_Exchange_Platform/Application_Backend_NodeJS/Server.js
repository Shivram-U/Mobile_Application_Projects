// Modules:
    const express = require("express");
    const http = require('http');

    const app = express();

    const path = require('path');

    const fs = require('fs');

    // User-defined Modules:
        const { Read_JSON,Update_JSON } = require("./JSON_FT");
        const {Connect_Database,Signup_User,Login_User,Get_User_Data,Register_Book,Get_User_Books,Update_Book,Search_Books} = require("./Database_IF");

// Constants and Paths:
    AVL = false;
    // Specify the path to your JSON file
    let statef;

// Files
    Read_JSON('./Data/State.json', (result) => {
      if (result !== null) {
        statef = result;
      } else {
        console.log('Failed to read JSON.');
        exit();
      }
    });


// Functions:
    function exit()
    {
        console.error('One or more conditions failed. Exiting the application.');

          // You can specify an exit code (0 for success, non-zero for error)
          process.exit(1); // Exit with an error code
    }

// File reading:


// Requirements Check:
    // Check conditions
// Request Services:

    app.get('/', (req, res) => {
        // Replace 'path/to/your/file.txt' with the actual path to your file
        const filePath = path.join(__dirname, 'index.html');

        console.log("Base Request");
        // Sending the file as a response
        res.sendFile(filePath, (err) => {
            if (err) {
                // Handle error, for example, send an error response
                res.status(500).send('Error sending the file');
            }
        });
    });

    app.get('/Book_Exchange_Application/DBCheck/', (req, res) => {
      // Access parameters
      Connect_Database((error,dbc) => {
        if (error) {
            console.error('Error connecting to the database:', error);
            // res.status(500).send('Error connecting to the database');
            return;
        }
        console.log("Database Connection Successfull");
        // Close the connection when done
        // Send a response
        dbc.end((err) => {
          if (err) {
            console.error('Error closing the connection:', err);
          } else {
            console.log('Database Connection closed');
          }
        });
        res.status(200).send('Connected to the database successfully');
      });

    });

    /*
        Test URLS:
            1. http://127.0.0.1:3000/Book_Exchange_Application/Signup?username=shivram&password=shivram&address=shivram&phonenumber=shivram&email=shivram&
    */
    app.get('/Book_Exchange_Application/Signup/', (req, res) => {

            // Get the current date
            const currentDate = new Date();

            // Get the current year as a string
            const currentYearAsString = currentDate.getFullYear().toString();

            console.log(currentYearAsString);
            var id = ("BEPUS["+String(statef.Last_ID)+"]@"+currentYearAsString);
            Signup_User(id,req.query.username,req.query.password,req.query.address,req.query.phonenumber,req.query.email,(error,message) => {
                    if(message == "Registration Authorized")
                       {
                            statef.Last_ID++;
                            Update_JSON(statef);
                       }
                    res.status(200).send(message);
              });
        });

       /*
            Test URLS:
                1. http://127.0.0.1:3000/Book_Exchange_Application/Login?username=shivram&password=shivram&
       */
       app.get('/Book_Exchange_Application/Login/', (req, res) => {
                   Login_User(req.query.username,req.query.password,(error,message) => {
                           if(message == "Sign-up successful")
                              {
                                   statef.Last_ID++;
                                   Update_JSON(statef);
                              }
                              console.log(message);
                           res.status(200).send(message);
                     });
               });


    app.get('/Book_Exchange_Application/User_Data/', (req, res) => {
                Get_User_Data(req.query.username,(error,message,data) => {
                        console.log(message);
                        res.status(200).send(data);
                  });
            });
    /*
        Test URLS:
            1. http://127.0.0.1:3000/Book_Exchange_Application/Book_Register?userid=4&name=bk&isbn=12&genre=ks&author=kk&edition=spn&purchasedate=ggy&language=english&copycount=1&condition=ck&
   */
    app.get('/Book_Exchange_Application/Book_Register/', (req, res) => {
      console.log(req.query);
      // Access Parameters :
      var data = [req.query.userid,req.query.name,req.query.isbn,req.query.genre,req.query.author,req.query.edition,req.query.purchasedate,req.query.language,req.query.copycount,req.query.condition];

      Register_Book(data,(error,message,data) => {
              console.log(message);
        });

      res.status(200).send("Book Registered");
    });

    app.get('/Book_Exchange_Application/Book_Update/', (req, res) => {
      // Access Parameters :
      console.log(req.query);
      var data = [req.query.name,req.query.isbn,req.query.genre,req.query.author,req.query.edition,req.query.purchasedate,req.query.language,req.query.copycount,req.query.condition,req.query.bookid];
      Update_Book(data,(error,message,data) => {
              console.log(message);
              if(message == "Success")
                res.status(200).send("Book Info Updated");
              else
                res.status(200).send("Book Info not Updated");
        });

    });

    app.get('/Book_Exchange_Application/Get_User_Books/', (req, res) => {
      // Access Parameters :
      console.log("Get_User_books"+req.query);
      console.log(req.query.userid);
      Get_User_Books(req.query.userid,(error,message,data) => {
              console.log(message);
              res.status(200).send(data);
        });

    });

    app.get('/Book_Exchange_Application/Search_Books/', (req, res) => {
      // Access Parameters :
      Search_Books(req.query,(error,message,data) => {
        console.log(message);
        res.status(200).send(data);
      });
    });

// Server Startup:
    const PORT = 3000;
    app.listen(PORT, () => {
        console.log(`Server is running on port ${PORT}`);
    });