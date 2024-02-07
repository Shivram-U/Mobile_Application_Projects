// Modules
    const mysql = require("mysql2");
    const fs = require('fs');
    const { format } = require('date-fns');

// User-defined Modules
    const { Read_JSON } = require("./JSON_FT");
const { text } = require("express");

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

}

function Signup_User(id,name,pword,address,phn,mail,callback) {
  const connection = mysql.createConnection({
    host: db.host,
    user: db.username,
    password:db.password,
    database: db.database,
  });

  connection.connect((err) => {
    if (err) {
      console.error('Error connecting to MySQL:', err.message);
      callback(err, 'Server Error, Please Try again later.');
      return;
    }

    // console.log('Connected to MySQL');
    // Data Logic:
        const sqlQuery1 = 'INSERT INTO userprofile(userid, name, address, contactno, emailid) VALUES (?,?,?,?,?)';
        const sqlQuery2 = 'INSERT INTO userauth(name,password,emailid) VALUES (?,?,?)';
        const values1=[id,name,address,phn,mail];
        const values2=[name,pword,mail];

        connection.query(sqlQuery1, values1,(error, results, fields) => {
          // Handle errors
          if (error) {
            console.error('Error executing query:', error);
            connection.end((err) => {
                      if (err) {
                        console.error('Error closing the connection:', err);
                      } else {
                        console.log('Database Connection closed');
                      }
                    });
             // Check for specific keywords in the error message
                if (error.message.includes('EmailId'))
                    callback(error, 'Email ID already exists, please enter an unique mail ID.');
                else if (error.message.includes('Name'))
                    callback(error, 'Username already exists, please enter an unique user-name.');
                else
                    // If none of the specific keywords are found, provide a generic error message
                    callback(error, 'Server Error, Please Try again later.');

            return;
          }
          connection.query(sqlQuery2,values2,(error,results,fields) => {
                if (error) {
                    console.error('Error executing query:', error);
                    return;
                }
          });
          // Process the results
            // console.log('Insert successful:', results);
            connection.end((err) => {
                      if (err) {
                        console.error('Error closing the connection:', err);
                      } else {
                        console.log('Database Connection closed');
                      }
                    });
            callback(null, "Registration Authorized");
        });
  });


}

function Login_User(name,password,callback) {
  const connection = mysql.createConnection({
    host: db.host,
    user: db.username,
    password:db.password,
    database: db.database,
  });

  connection.connect((err) => {
    if (err) {
      console.error('Error connecting to MySQL:', err.message);
      callback(err, 'Server Error, Please Try again later.');
      return;
    }

    // console.log('Connected to MySQL');
    // Data Logic:
        const sqlQuery = 'select password from userauth where name=?';
        const values=[name];

        connection.query(sqlQuery, values,(error, results, fields) => {
          // Handle errors
          if (error) {
            console.error('Error executing query:', error);
            connection.end((err) => {
                      if (err) {
                        console.error('Error closing the connection:', err);
                      } else {
                        console.log('Database Connection closed');
                      }
                    });

            return;
          }


            // console.log('Insert successful:', results);
            connection.end((err) => {
                      if (err) {
                        console.error('Error closing the connection:', err);
                      } else {
                        console.log('Database Connection closed');
                      }
                    });

            // console.log(results);
            // console.log(results[0].password);
            // console.log(fields);
                      // Process the results
            if(results.length == 0)
                callback(null, "Username does not exists");
            else
            {
                chk = true;
                results.forEach((element, index) => {
                    if(element.password == password)
                    {
                        chk = false;
                        return;
                    }
                });
                if(chk)
                    callback(null, "Password Incorrect");
                else
                    callback(null,"Login Authorized");
            }
        });
  });
}

function Get_User_Data(name,callback) {
  const connection = mysql.createConnection({
    host: db.host,
    user: db.username,
    password:db.password,
    database: db.database,
  });

  connection.connect((err) => {
    if (err) {
      console.error('Error connecting to MySQL:', err.message);
      callback(err, 'Server Error, Please Try again later.');
      return;
    }

    // console.log('Connected to MySQL');
    // Data Logic:
        const sqlQuery = 'select userid,name,joineddate,reputationscore,address,contactno,emailid from userprofile where name=?';
        const sqlQuery1 = 'select password from userauth where name=?';
        const values=[name];
        const ans=[]
        var password = null;
        let jsonobj = {};

        connection.query(sqlQuery1, values,(error, results, fields) => {
              // Handle errors
              if (error) {
                console.error('Error executing query:', error);
                connection.end((err) => {
                          if (err) {
                            console.error('Error closing the connection:', err);
                          } else {
                            console.log('Database Connection closed');
                          }
                        });
                        callback(null,"Error");
                return;
              }
                 // console.log('Insert successful:', results);
            results.forEach((element, index) => {
                  jsonobj.password = element.password;
            });
        });
        connection.query(sqlQuery, values,(error, results, fields) => {
          // Handle errors
          if (error) {
            console.error('Error executing query:', error);
            connection.end((err) => {
                      if (err) {
                        console.error('Error closing the connection:', err);
                      } else {
                        console.log('Database Connection closed');
                      }
                    });
                    callback(null,"Error",null);
            return;
          }



            // console.log(results);
            // console.log(results[0].password);
            // console.log(fields);
                      // Process the results
            // console.log(results);
            const currentDate = new Date();

            // Get the current year as a string
            const currentYearAsString = currentDate.getFullYear().toString();
            element = results[0];
            console.log(element);
            jsonobj.userid = element.userid;
            jsonobj.username = element.name;
            jsonobj.address = element.address;
            jsonobj.joineddate = element.joineddate.toLocaleDateString();
            jsonobj.reputationscore = element.reputationscore;
            jsonobj.contactno = element.contactno;
            jsonobj.email = element.emailid;
            connection.end((err) => {
              if (err) {
                console.error('Error closing the connection:', err);
              } else {
                console.log('Database Connection closed');
              }
            });
            // console.log(ans);
            callback(null,"Success",JSON.stringify(jsonobj, null, 2));
        });
  });
}

function Register_Book(dat,callback) {
  const connection = mysql.createConnection({
    host: db.host,
    user: db.username,
    password:db.password,
    database: db.database,
  });

  connection.connect((err) => {
    if (err) {
      console.error('Error connecting to MySQL:', err.message);
      callback(err, 'Server Error, Please Try again later.');
      return;
    }

    // console.log('Connected to MySQL');
    // Data Logic:
        const sqlQuery = 'select bookscount from userprofile where userid=?';
        const sqlQuery1 = 'insert into books values(?,?,?,?,?,?,?,?,?,?,?)';
        const sqlQuery2 = 'update userprofile set bookscount=? where userid=?';
        var values=[dat[0]];
        var bkc;

        connection.query(sqlQuery, values,(error, results, fields) => {
              // Handle errors
              if (error) {
                console.error('Error executing query:', error);
                connection.end((err) => {
                          if (err) {
                            console.error('Error closing the connection:', err);
                          } else {
                            console.log('Database Connection closed');
                          }
                        });
                        callback(null,"Error");
                return;
              }
              bkc = results[0].bookscount;
              values=[bkc+1,dat[0]]
              connection.query(sqlQuery2,values,(error, results, fields) => {
                 // Handle errors
                 if (error) {
                   console.error('Error executing query:', error);
                   connection.end((err) => {
                             if (err) {
                               console.error('Error closing the connection:', err);
                             } else {
                               console.log('Database Connection closed');
                             }
                           });
                           callback(null,"Error");
                   return;
                   }
                 });

              var values=[dat[0],String(dat[0])+"#"+String(bkc+1),dat[1],dat[2],dat[3],dat[4],dat[5],dat[6],dat[7],dat[8],dat[9]];
              connection.query(sqlQuery1, values,(error, results, fields) => {
                    // Handle errors
                    if (error) {
                      console.error('Error executing query:', error);
                      connection.end((err) => {
                                if (err) {
                                  console.error('Error closing the connection:', err);
                                } else {
                                  console.log('Database Connection closed');
                                }
                              });
                              callback(null,"Error");
                          return;
                    }
                    console.log('Insert successful:', results);
              });
              connection.end((err) => {
                      if (err) {
                        console.error('Error closing the connection:', err);
                      } else {
                        console.log('Database Connection closed');
                      }
                    });
              callback(null,"Success");
        });

  });
}
function Update_Book(data,callback) {
  const connection = mysql.createConnection({
    host: db.host,
    user: db.username,
    password:db.password,
    database: db.database,
  });

  connection.connect((err) => {
    if (err) {
      console.error('Error connecting to MySQL:', err.message);
      callback(err, 'Server Error, Please Try again later.');
      return;
    }

    // console.log('Connected to MySQL');
    // Data Logic:
        const sqlQuery = 'update books set Name=?,ISBN=?,Genre=?,Author=?,Edition=?,PurchaseDate=?,Language=?,copycount=?,bkcondition=? where bookid=?';     
        connection.query(sqlQuery, data,(error, results, fields) => {
              // Handle errors
              if (error) {
                console.error('Error executing query:', error);
                connection.end((err) => {
                          if (err) {
                            console.error('Error closing the connection:', err);
                          } else {
                            console.log('Database Connection closed');
                          }
                        });
                        callback(null,"Error");
                return;
              }
              connection.end((err) => {
                      if (err) {
                        console.error('Error closing the connection:', err);
                      } else {
                        console.log('Database Connection closed');
                      }
                    });
              callback(null,"Success");
        });

  });
}


function Get_User_Books(userid,callback) {
  console.log(db);
  const connection = mysql.createConnection({
    host: db.host,
    user: db.username,
    password:db.password,
    database: db.database,
  });

  connection.connect((err) => {
    if (err) {
      console.error('Error connecting to MySQL:', err.message);
      callback(err, 'Server Error, Please Try again later.');
      return;
    } 
    const sqlQuery = "SELECT * FROM books WHERE userid=? order by bookid"
    var values=[userid];
    let jsonobj = {};
    connection.query(sqlQuery, values,(error, results, fields) => {
        // Handle errors
        if (error) {
          console.error('Error executing query:', error);
          connection.end((err) => {
            if (err) {
              console.error('Error closing the connection:', err);
            } else {
              console.log('Database Connection closed');
            }
            return;
          });
          callback(null,"Error");
        }
        // console.log('Insert successful:', results);
        for(i=0;i<results.length;i++)
        {
          const purchaseDate = new Date(results[i].PurchaseDate);
          const formattedDate = format(purchaseDate, 'dd MMM yyyy');
          console.log(formattedDate);
          results[i].PurchaseDate = formattedDate;
          jsonobj[i] = results[i];
        }
        callback(null,"Success",JSON.stringify(jsonobj, null, 2));
    });

    connection.end((err) => {
      if (err) {
        console.error('Error closing the connection:', err);
      } else {
        console.log('Database Connection closed');
      }
    });
  });
  return;
}

function Search_Books(data,callback) {
  console.log(db);
  const connection = mysql.createConnection({
    host: db.host,
    user: db.username,
    password:db.password,
    database: db.database,
  });

  var sql = "select * from books where "
  var values = []
  var test = false;
  // console.log("meow",data);
  if(data.hasOwnProperty("name"))
  {
    sql+="name = ? ";
    test = true;
    values.push(data.name);
  }
  if(data.hasOwnProperty("language"))
  {
    if(test)
      sql+="AND Language = ? ";
    else
    {
      sql+="Language = ? ";
      test = true;
    }
    values.push(data.language);
  }
  if(data.hasOwnProperty("genre"))
  {
    if(test)
      sql+="AND genre = ? ";
    else
    {
      sql+="genre = ? ";
      test = true;
    }
    values.push(data.genre);
  }
  if(data.hasOwnProperty("author")) 
  {
    if(test)
      sql+="AND author = ? ";
    else
    {
      sql+="author = ? ";
      test = true;
    }
    values.push(data.author);
  }

  connection.connect((err) => {
    if (err) {
      console.error('Error connecting to MySQL:', err.message);
      callback(err, 'Server Error, Please Try again later.');
      return;
    } 
    let jsonobj = {};
    connection.query(sql, values,(error, results, fields) => {
        // Handle errors
        if (error) {
          console.error('Error executing query:', error);
          connection.end((err) => {
            if (err) {
              console.error('Error closing the connection:', err);
            } else {
              console.log('Database Connection closed');
            }
            return;
          });
          callback(null,"Error");
        }
        for(i=0;i<results.length;i++)
        {
          const purchaseDate = new Date(results[i].PurchaseDate);
          const formattedDate = format(purchaseDate, 'dd MMM yyyy');
          // console.log(formattedDate);
          results[i].PurchaseDate = formattedDate;
          jsonobj[i] = results[i];
        }
        callback(null,"Success",JSON.stringify(jsonobj, null, 2));
    });
    connection.end((err) => {
      if (err) {
        console.error('Error closing the connection:', err);
      } else {
        console.log('Database Connection closed');
      }
    });
  });
  return;
}

// Module Exports:
    module.exports = {
      Connect_Database,
      Signup_User,
      Login_User,
      Get_User_Data,
      Register_Book,
      Get_User_Books,
      Update_Book,
      Search_Books
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
