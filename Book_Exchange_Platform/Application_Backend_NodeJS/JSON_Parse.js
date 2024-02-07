const fs = require('fs');

function Read_JSON(statef, callback) {
  console.log(statef);
  fs.readFile(statef, 'utf8', (err, data) => {
    if (err) {
      console.error('Error reading JSON file:', err);
      callback(null); // Call the callback with null if there's an error
      return;
    }

    console.log("meow");

    try {
      // Parse the JSON data
      const jsonData = JSON.parse(data);
      console.log(jsonData);
      callback(jsonData); // Call the callback with the parsed JSON data
    } catch (parseError) {
      console.error('Error parsing JSON:', parseError);
      callback(null); // Call the callback with null if there's a parsing error
    }
  });
}

// Example usage:
Read_JSON('./Data/State.json', (result) => {
  if (result !== null) {
    console.log('Successfully read JSON:', result);
  } else {
    console.log('Failed to read JSON.');
  }
});