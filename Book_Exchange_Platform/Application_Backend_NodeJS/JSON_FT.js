const fs = require('fs');

function Read_JSON(statef, callback) {
  fs.readFile(statef, 'utf8', (err, data) => {
    if (err) {
      console.error('Error reading JSON file:', err);
      callback(null);
      return;
    }

    try {
      const jsonData = JSON.parse(data);
      callback(jsonData);
    } catch (error) {
      console.error('Error parsing JSON:', error);
      callback(null);
    }
  });
}

function Update_JSON(json, callback) {
    // Convert the modified data back to a JSON string
    let modifiedJsonString = JSON.stringify(json, null, 2);

    // Write the modified data back to the file
    fs.writeFile('./Data/State.json', modifiedJsonString, 'utf8', (err) => {
        if (err) {
            console.error('Error writing file:', err);
        } else {
            console.log('File has been modified successfully.');
        }
    });
}


module.exports = {
    Read_JSON,
    Update_JSON
};