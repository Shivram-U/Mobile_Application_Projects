// Modules:
    const express = require("express");
    const http = require('http');

    const app = express();

    const path = require('path');

    const fs = require('fs');
// User-defined Modules
    const {Connect_Database,Signup_User,Login_User,Get_User_Data,Register_Book,Get_User_Books} = require("./Database_IF");

setTimeout(() => {
        Get_User_Books("BEPUS[6]@2024",(req, res,data) => {
            console.log(res);
            res.status(200).send(data);
        });

}, 2000);

// Code after the delay
console.log("End");

