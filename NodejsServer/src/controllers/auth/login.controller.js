const hashHelper = require("../../helpers/password-encrypter");

const { getUserByUsername } = require("../CRUD/user");

const login = async (req, res) => {
    try {
        const { username, password } = req.body

        const user = await getUserByUsername(username)

        if (!user)
            return res.status(404).json({ message: "User not found!!" })

        const isPasswordCorrect = await hashHelper.compare(password, user.password.trim())

        if (!isPasswordCorrect) {
            return res.status(401).json({
                message: "Password is incorrect"
            })
        }

        res.status(200).json({
            message: "Login success!!",
            user: user
        })
    }
    catch (error) {
        console.error(error);

        // In ra lỗi từ Sequelize
        if (error.errors) {
            error.errors.forEach((err) => {
                console.error(err.message);
            });
        }

        return res.status(400).json({
            message: "Error Login!!!",
            error: error.message,
        });
    }
}



module.exports = login;
