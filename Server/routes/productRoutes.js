const express = require('express');
const router = express.Router();
const { getProductByUserID } = require('../controllers/productController');
const { getProductById } = require('../controllers/productController');
const { addProduct } = require('../controllers/productController');
const { updateProduct } = require('../controllers/productController');
const { deleteProduct } = require('../controllers/productController');

router.get('/user/:id', getProductByUserID);
router.get('/:id', getProductById);
router.post('/add', addProduct);
router.put('/update/:id', updateProduct);
router.delete('/delete/:id', deleteProduct);

module.exports = router;