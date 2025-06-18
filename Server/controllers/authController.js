const db = require('../config/db.js');
const bcrypt = require('bcrypt');
const generateTempPassword = require('../utils/generateTempPassword.js');
const sendTempPassword = require('../utils/sendTempPassword.js');


// Signup API
exports.signup = (req, res) => {

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

        const hashedPassword = await bcrypt.hash(password, 10);
        const insertQuery = 'INSERT INTO users (name, email, password, shop_name) VALUES (?, ?, ?, ?)';


        db.query(insertQuery, [name, email, hashedPassword, shop_name], (err) => {
            if (err) return res.status(500).json({ 
                success: false, 
                message: 'Failed to register user' 
            });

            res.status(201).json({ 
                success: true, 
                message: 'User registered successfully' });
        });
    });
};

// Login API
exports.login = (req, res) => {
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
            success : true,
            message: 'Login successful',
            user: {
                id: user.id,
                name: user.name,
                email: user.email,
                shop_name: user.shop_name
            }
        });
    });
};

// Get all users API
exports.users = (req, res) => {

    const query = 'SELECT * FROM users';
    db.query(query, (err, results) => {
        
        if(err){
            return res.status(500).json({
                success : false,
                message : 'Internal server error'});
        }
        res.json(results);
    });
};

//Forgot Password API
exports.forgotPassword = (req, res) => {

     const { email } = req.body;

      if(!email){
        return res.status(400).json({
            success : false,
            message: 'Email is required'
        });
    }

     const query = 'SELECT * FROM users WHERE email = ?';

     db.query(query, [email], async (err, results) => {

        if (err || results.length === 0) {
            return res.status(404).json({
                success : false,
                message: 'User not found'
            });
        }

        //Generate Temp Password
        const TempPassword = generateTempPassword();
        const hashedPassword = await bcrypt.hash(TempPassword, 10);


        //Update in database
        
        const updateQuery = 'UPDATE users SET password = ? WHERE email = ?';
        
        db.query(updateQuery, [hashedPassword, email], (err, result) => {

            if(err){
                res.status(500).json({
                    success : false,
                    message: 'Failed to update password'
                });
            }

            //Send email with temporary password
            sendTempPassword(email, TempPassword);
            return res.status(200).json({
                success : true, 
                message : 'Check your mail'
            });    
        });
    });

};
