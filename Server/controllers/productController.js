const db = require('../config/db.js');

//Get all products by user ID
exports.getProductByUserID = (req, res) => {

    const { id } = req.params;

    const query = 'SELECT * FROM products WHERE user_id = ?';

    db.query(query, [id], (err, results) =>{
        if(err){
            res.status(500).json({
                success: false,
                message: 'Internal server error'
            });
        }

        if(results.length === 0){
            return res.status(404).json({
                success: false,
                message: 'No products found for this user'
            });
        }

        res.status(200).json({
            success: true,
            products: results
        });
    });
};

//Get product by ID
exports.getProductById = (req, res) => {

    const { id } = req.params;

    const query = 'SELECT * FROM products WHERE id = ?';

    db.query(query, [id], (err, results) => {

        if(err){
            return res.status(500).json({
                success: false,
                message: 'Internal server error'
            });
        }

        if(results.length === 0){
            return res.status(404).json({
                success: false,
                message: 'Product not found'
            });
        }

        res.status(200).json({
            success: true,
            product: results[0]
        });
    });
};


//Add new product
exports.addProduct = (req, res) => {

    const {
        user_id,
        type,
        company,
        model_name,
        serial_number,
        months_of_warranty,
        purchase_date,
        quantity,
        price
    } = req.body;

    if(!user_id || !type || !company || !model_name || !serial_number || !months_of_warranty || !purchase_date || !quantity || !price){
        return res.status(400).json({
            success: false,
            message: 'All fields are required'
        });
    }

    const query = 'INSERT INTO products (user_id, type, company, model_name, serial_number, months_of_warranty, purchase_date, quantity, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)';

    const values = [user_id, type, company, model_name, serial_number, months_of_warranty, purchase_date, quantity, price];

    db.query(query, values, (err, results) => {

        if(err){
            if (err.code === 'ER_DUP_ENTRY') {
                return res.status(409).json({ 
                    success: false,
                    message: 'Serial number already exists.'
                 });
            }
            return res.status(500).json({
                success: false,
                message: 'Internal server error'
            });
        }

        res.status(201).json({
            success: true,
            message: 'Product added successfully',
            product: {
                id: results.insertId,
                user_id,
                type,
                company,
                model_name,
                serial_number,
                months_of_warranty,
                purchase_date,
                quantity,
                price
            }
        });
    });
};

// Update product by ID
exports.updateProductById = (req, res) => {

    

};

