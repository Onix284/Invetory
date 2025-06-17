const express = require('express');
const cors = require('cors');
const app = express();
const authRoutes = require('./routes/authRoutes.js');

app.use(express.json());
app.use(cors());

app.use('/auth', authRoutes);

app.get('/', (req, res) => {
    res.send("Server is running!");
});

const PORT = 3000;
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});