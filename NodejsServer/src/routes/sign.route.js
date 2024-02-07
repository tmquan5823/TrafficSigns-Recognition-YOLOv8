const express = require("express");
const router = express.Router();
const signController = require('../controllers/api/sign.controller')

router.get('/all-sign', signController.getListSign)
router.get('/detect', signController.regconizeSign)

module.exports = router