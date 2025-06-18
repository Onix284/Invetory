const express = require('express');
const router = express.Router();
const { getUserById } = require('../controllers/userController.js')

router.get('/:id', getUserById);

module.exports = router;