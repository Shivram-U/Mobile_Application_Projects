// Modules
    const mysql = require("mysql2");
    const fs = require('fs');

// User-defined Modules
    const { Read_JSON } = require("./JSON_FT");

// Constants
    let db;

// Files
    Read_JSON('./Data/DB_Auth.json', (result) => {
      if (result !== null) {
        db = result;
        // console.log("DB :"+db.host);
        // Once 'db' is set, you can proceed with connecting to the database or perform other actions.
      } else {
        console.log('Failed to read JSON.');
        process.exit(); // Assuming you want to exit the process on failure
      }
    });

// Functions

function Connect_Database(callback) {
  const connection = mysql.createConnection({
    host: db.host,
    user: db.username,
    password:db.password,
    database: db.database,
  });

  connection.connect((err) => {
    if (err) {
      console.error('Error connecting to MySQL:', err.message);
      callback(err, null);
      return;
    }

    // console.log('Connected to MySQL');
    callback(null, connection);
  });

  connection.on('error', (err) => {
    console.error('MySQL connection error:', err.message);
    callback(err, null);
  });
}

// Module Exports:
    module.exports = {
      Connect_Database
    };


/*
    Database Interaction:
        // Execute a query
        const sqlQuery = 'SELECT * FROM your_table';

        connection.query(sqlQuery, (error, results, fields) => {
          // Handle errors
          if (error) {
            console.error('Error executing query:', error);
            connection.end(); // Close the connection in case of an error
            return;
          }

          // Process the results
          console.log('Query results:', results);

          // Access individual rows
          for (const row of results) {
            console.log('Row:', row);
          }

          // Access fields metadata
          console.log('Fields metadata:', fields);
        });
*/
