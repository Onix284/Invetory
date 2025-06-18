const db = require('../config/db.js');

// Get user by ID API
exports.getUserById = (req, res) => {

    const { id } = req.params;
    
    const query = 'SELECT id, name, email, shop_name FROM users WHERE id = ?';


    db.query(query, [id], (err, result) => {

        if(err){
            return res.status(500).json({
                success : false,
                message: 'Internal server error'
            });
        }

        if(result.length === 0){
            return res.status(404).json({
                success : false,
                message: 'User not found'
            });
        }

        res.status(200).json({
            success: true,
            user: result[0]
        });
    });
}