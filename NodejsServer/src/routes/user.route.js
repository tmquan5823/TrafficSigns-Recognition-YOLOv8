const express = require("express");
const router = express.Router();
const userController = require('../controllers/api/user.controller')


router.get('/all-user', userController.getListUser)
router.get('/:id', userController.getUserById)
router.post('/update-user', userController.updateUserId)
router.post('/change-pass', userController.changePassword)

module.exports = router