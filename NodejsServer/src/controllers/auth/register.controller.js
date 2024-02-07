const hashHelper = require('../../helpers/password-encrypter');
const { addUser, getUserByUsername } = require('../CRUD/user')
const path = require('path')
const fs = require('fs')
const staticPath = path.join(__dirname, '../../public/avatar/default.png');
const defaultAvatar = fs.readFileSync(staticPath)


const register = async (req, res) => {
    try {
        const checkUser = await getUserByUsername(req.body.username)
        if (checkUser) {
            return res.status(409).json({
                message: "This username has been used!!!"
            })
        }
        console.log(checkUser)
        const newUser = {
            username: req.body.username,
            password: hashHelper.hash(req.body.password),
            name: req.body.name,
            phone: req.body.phone,
            email: req.body.email,
            address: req.body.address,
            avatar: defaultAvatar,
        };
        await addUser(newUser);
        res.status(200).json({
            message: "Create user successfully!",
        });

    } catch (error) {
        console.error(error);

        // In ra lỗi từ Sequelize
        if (error.errors) {
            error.errors.forEach((err) => {
                console.error(err.message);
            });
        }

        return res.status(400).json({
            message: "Error Create User!!!",
            error: error.message,
        });
    }

}

module.exports = register