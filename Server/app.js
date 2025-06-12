const express = require('express');
const db = require('./db');
const cors = require('cors');


const app = express();
app.use(express.json());
app.use(cors());

const PORT = process.env.PORT || 3000;

app.post('/auth/signup', (req, res) => {

    const { name, email, password, shopname } = req.body;

    if (!name || !email || !password || !shopname) {
        return res.status(400).send('All fields are required');
    }

    const query = 'INSERT INTO users (name, email, password, shopname ) VALUES (?, ?, ?, ?)';
    
    const values = [name, email, password, shopname];

    db.query(query, values, (err, result) => {

        if(err){
            console.error('Error inserting user:', err);
            return res.status(500).send('Internal server error');
        }
    });

    res.status(201).json({ message: 'User registered successfully' });
});

app.listen(PORT, () => {

    console.log(`Server is running on port ${PORT}`);

});
