const mysql = require('mysql2');

const db = mysql.createConnection({
    host : 'localhost',
    user :  'root',
    password : 'Onix@8210',
    database : 'invetory'
});

db.connect(err => {
    if(err){
        console.error('Error connecting to the database:', err);
    }
});

module.exports = db;