const express = require('express');
const router = express.Router();
const { getProductByUserID } = require('../controllers/productController');
const { getProductById } = require('../controllers/productController');
const { addProduct } = require('../controllers/productController');

router.get('/user/:id', getProductByUserID);
router.post('/add', addProduct);
router.get('/:id', getProductById);

module.exports = router;