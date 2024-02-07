const express = require("express");
const router = express.Router();
const positionController = require('../controllers/api/position.controller')

router.post('/all-position-id', positionController.getListPositionById)
router.post('/create-position', positionController.createPosition)

module.exports = router