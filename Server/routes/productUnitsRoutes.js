const express = require('express');
const router = express.Router();

const { addProductUnits } = require('../controllers/productUnitsController');
const { getUnitsByProductId } = require('../controllers/productUnitsController');
const { updateProductUnit } = require('../controllers/productUnitsController');
const { deleteProductUnit } = require('../controllers/productUnitsController');

router.post('/add', addProductUnits);
router.get('/get/:product_id', getUnitsByProductId);
router.put('/update/:id', updateProductUnit);
router.delete('/delete/:id', deleteProductUnit);

module.exports = router;