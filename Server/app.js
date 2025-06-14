const express = require('express');
const db = require('../Server/config/db.js');
const cors = require('cors');
const bcrypt = require('bcrypt');

const app = express();
app.use(express.json());
app.use(cors());

const PORT = process.env.PORT || 3000;

//Signup API
app.post('/auth/signup', (req, res) => {

    const { name, email, password, shop_name } = req.body;

    if (!name || !email || !password || !shop_name) {
        return res.status(400).json({
            success : false,
            message: 'All fields are required'
        });
    }

    // Step 1: Check if user already exists
    const checkQuery = 'SELECT * FROM users WHERE email = ?';

    db.query(checkQuery, [email], async (err, results) => {

        if (err) return res.status(500).json({
            success : false,
            message: 'Database error'
        });

        if (results.length > 0) {
            return res.status(409).json({
                success : false, 
                message : 'User already exists' 
            });
        }

    // Step 2: Hash the password before storing
    const saltRounds = 10;
    const hashedPassword = await bcrypt.hash(password, saltRounds);

      // Step 3: Insert user into DB
    const insertQuery = 'INSERT INTO users (name, email, password, shop_name) VALUES (?, ?, ?, ?)';

    db.query(insertQuery, [name, email, hashedPassword, shop_name], (err, result) => {
      if (err) {
        console.error('Insert error:', err);
        return res.status(500).json({
            success : false,
            message : 'Failed to register user'
        });
      }
       res.status(201).json({ 
            success : true,
            message: 'User registered successfully' });
     });
   });
});


app.post('/auth/login', (req, res) => {

    const { email, password } = req.body;
    
    if(!email || !password){
        return res.status(400).json({
            success : false,
            message: 'Email and password are required'});
    }

    const query = 'SELECT * FROM users WHERE email = ?';

    db.query(query, [email], async (err, results) => {

        if(err){
            console.error('Error fetching user:', err);
            return res.status(500).json({
                success : false, 
                message : 'Internal server error'});
        }

        if(results.length === 0){
            return res.status(401).json({
                success : false,  
                message : 'Invalid email' });
        }

        const user = results[0];

        //Compare password with hashed password
        const match = await bcrypt.compare(password, user.password);
        if(!match){
            return res.status(401).json({ 
                success : false, 
                message : 'Invalid password' });
        }
        

        res.status(200).json({
            message: 'Login successful',
            user: {
                id: user.id,
                name: user.name,
                email: user.email,
                shop_name: user.shop_name
            }
        });

    });

});


app.get('/users', (req, res) => {

    const query = 'SELECT * FROM users';

    db.query(query, (err, results) => {
        
        if(err){
            return res.status(500).json({
                success : false,
                message : 'Internal server error'});
        }
        res.json(results);
    });
});

app.listen(PORT, () => {

    console.log(`Server is running on port ${PORT}`);

});

