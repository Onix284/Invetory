const db = require('../config/db.js');

//Add new product unit
exports.addProductUnits = (req, res) => {

    const { product_id, serial_number} = req.body;

    if (!product_id || !serial_number) {
        return res.status(400).json({
            success: false,
            message: 'Product ID and serial number are required'
        });
    }

    const serials = Array.isArray(serial_number) ? serial_number : [serial_number];

    if (serials.length === 0) {
        return res.status(400).json({
            success: false,
            message: 'At least one serial number is required'
        });
    }

    const values = serials.map(sn => [product_id, sn]);

    const query = 'INSERT INTO product_units (product_id, serial_number) VALUES ?';
  

    db.query(query, [values], (err, results) => {
        if (err) {

            if (err.code === 'ER_DUP_ENTRY') {
                return res.status(409).json({
                    success: false,
                    message: "One or more serial numbers already exist",
                });
            }

            return res.status(500).json({
                success: false,
                message: 'Failed to add product units',
                error: err.message
            });
        }

        res.status(201).json({
            success: true,
            message: 'Product units added successfully',
            data: {
                affectedRows: results.affectedRows
            }
        });
    });
};  


//Get all product units details based on Product id
exports.getUnitsByProductId = (req, res) => {

    const { product_id } = req.params;
    console.log(product_id);

    const query = 'SELECT * FROM product_units WHERE product_id = ?';

    db.query(query, [product_id], (err, results) => {
        if (err) {
            return res.status(500).json({
                success: false,
                message: 'Failed to fetch product units',
                error: err.message
            });
        }

        res.status(200).json({
            success: true,
            units: results
        });
    });
};


//Update serial number based on product units id
exports.updateProductUnit = (req, res) => {
    const { id } = req.params;
    const { serial_number } = req.body;

    if (!serial_number) {
        return res.status(400).json({
            success: false,
            message: 'Serial number is required'
        });
    }

    const query = 'UPDATE product_units SET serial_number = ? WHERE id = ?';

    db.query(query, [serial_number, id], (err, result) => {
        if (err) {
            if (err.code === 'ER_DUP_ENTRY') {
                return res.status(409).json({
                    success: false,
                    message: 'This serial number already exists'
                });
            }

            return res.status(500).json({
                success: false,
                message: 'Failed to update product unit',
                error: err.message
            });
        }

        if (result.affectedRows === 0) {
            return res.status(404).json({
                success: false,
                message: 'Product unit not found'
            });
        }

        res.status(200).json({
            success: true,
            message: 'Product unit updated successfully'
        });
    });
};


//Delete a product units based on id
exports.deleteProductUnit = (req, res) => {
    
    const { id } = req.params;

    const query = 'DELETE FROM product_units WHERE id = ?';

    db.query(query, [id], (err, result) => {
        if (err) {
            return res.status(500).json({
                success: false,
                message: 'Failed to delete product unit',
                error: err.message
            });
        }

        if (result.affectedRows === 0) {
            return res.status(404).json({
                success: false,
                message: 'Product unit not found'
            });
        }

        res.status(200).json({
            success: true,
            message: 'Product unit deleted successfully'
        });
    });
};
