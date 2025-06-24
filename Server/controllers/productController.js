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


// Add new product
exports.addProduct = (req, res) => {
    const {
        user_id,
        type,
        company,
        model_name,
        months_of_warranty,
        purchase_date,
        price,
        quantity
    } = req.body;

    if (!user_id || !type || !company || !model_name || !months_of_warranty || !purchase_date || !price || !quantity) {
        return res.status(400).json({
            success: false,
            message: 'All fields are required'
        });
    }

    const query = `
        INSERT INTO products (user_id, type, company, model_name, months_of_warranty, purchase_date, price, quantity)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    `;

    const values = [user_id, type, company, model_name, months_of_warranty, purchase_date, price, quantity];

    db.query(query, values, (err, result) => {
        if (err) {
            return res.status(500).json({
                success: false,
                message: 'Error inserting product',
                error: err
            });
        }

        res.status(201).json({
            success: true,
            message: 'Product added successfully',
            product: {
                id: result.insertId,
                user_id,
                type,
                company,
                model_name,
                months_of_warranty,
                purchase_date,
                price,
                quantity
            }
        });
    });
};

//Update product by ID
exports.updateProduct = (req, res) => {
    const {
        type,
        company,
        model_name,
        months_of_warranty,
        purchase_date,
        price,
        quantity
    } = req.body;

    const product_id = req.params.product_id; // ✅ take from URL

    if (!product_id || !type || !company || !model_name || !months_of_warranty || !purchase_date || !price || !quantity) {
        return res.status(400).json({
            success: false,
            message: "All fields are required",
        });
    }

    const query = `
    UPDATE products SET 
        type = ?, 
        company = ?, 
        model_name = ?, 
        months_of_warranty = ?, 
        purchase_date = ?, 
        price = ?, 
        quantity = ?
    WHERE id = ?
`;

  const values = [
    type,
    company,
    model_name,
    months_of_warranty,
    purchase_date,
    price,
    quantity,
    product_id
];

    db.query(query, values, (err, result) => {
        if (err) {
            return res.status(500).json({
                success: false,
                message: "Internal server error",
            });
        }

        if (result.affectedRows === 0) {
            return res.status(404).json({
                success: false,
                message: "Product not found",
            });
        }

        res.status(200).json({
            success: true,
            message: "Product updated successfully",
        });
    });
};


// Delete product by ID
exports.deleteProduct = (req, res) => {
    const { id } = req.params;

    if (!id) {
        return res.status(400).json({
            success: false,
            message: "Product ID is required",
        });
    }

    const query = "DELETE FROM products WHERE id = ?";

    db.query(query, [id], (err, result) => {
        if (err) {
            return res.status(500).json({
                success: false,
                message: "Internal server error",
            });
        }

        if (result.affectedRows === 0) {
            return res.status(404).json({
                success: false,
                message: "Product not found",
            });
        }

        res.status(200).json({
            success: true,
            message: "Product deleted successfully",
        });
    });
};
