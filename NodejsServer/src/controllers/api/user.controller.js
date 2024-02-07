const { showAllUser, showUserById, updateUserById, updatePassword } = require('../CRUD/user')
const hashHelpers = require('../../helpers/password-encrypter')
const { dataUriToBuffer } = require('data-uri-to-buffer')

const getListUser = async (req, res) => {
    try {
        const listUser = await showAllUser()
        res.status(200).json({
            message: "This is All User!!",
            listUser: listUser
        })
    } catch (error) {
        console.error(error);

        // In ra lỗi từ Sequelize
        if (error.errors) {
            error.errors.forEach((err) => {
                console.error(err.message);
            });
        }

        res.status(400).json({
            message: "Error Show List User!!!",
            error: error.message,
        });
    }
}

const getUserById = async (req, res) => {
    try {
        const userId = req.params.id;
        const user = await showUserById(userId);
        return res.status(200).json({
            message: "Success get user",
            user: user
        });
    } catch (error) {
        // In ra lỗi từ Sequelize
        if (error.errors) {
            error.errors.forEach((err) => {
                console.error(err.message);
            });
        }

        res.status(400).json({
            message: "Error Show User By Id!!!",
            error: error.message,
        });
    }
}

const updateUserId = async (req, res) => {
    try {
        const currentUser = await showUserById(req.body.id)
        // if (req.file) {
        //     const fileBuffer = req.file.buffer
        //     currentUser.avatar = fileBuffer;
        //     console.log(fileBuffer)
        // }
        const updateUser = {
            name: req.body.name ? req.body.name : currentUser.name,
            phone: req.body.phone ? req.body.phone : currentUser.phone,
            email: req.body.email ? req.body.email : currentUser.email,
            address: req.body.address ? req.body.address : currentUser.address,
            avatar: Buffer.from(req.body.avatar) ? Buffer.from(req.body.avatar) : currentUser.avatar,
        }

        await updateUserById(updateUser, req.body.id)
            .then(() => {
                res.status(200).json({
                    message: "Update User Success!"
                })
            })
            .catch(error => {
                res.status(400).json({
                    message: "Update User Error!!",
                    error: error.message
                })
            })
    } catch (error) {
        // In ra lỗi từ Sequelize
        if (error.errors) {
            error.errors.forEach((err) => {
                console.error(err.message);
            });
        }

        res.status(400).json({
            message: "Error Update User By Id!!!",
            error: error.message,
        });
    }
}

const changePassword = async (req, res) => {
    try {
        const { id, old_password, new_password } = req.body

        const updateUser = await showUserById(id)
        console.log(req.body.new_password)
        const isPasswordCorrect = await hashHelpers.compare(old_password, updateUser.password.trim())
        if (!isPasswordCorrect) {
            return res.status(400).json({
                message: "Your old password is wrong!!"
            })
        }
        await updatePassword(hashHelpers.hash(new_password), updateUser)
            .then(() => {
                res.status(200).json({
                    message: "Change Password Success!"
                })
            })
            .catch(error => {
                res.status(400).json({
                    message: "Change Password Error!!",
                    error: error.message
                })
            })
    } catch (error) {
        if (error.errors) {
            error.errors.forEach((err) => {
                console.error(err.message);
            });
        }

        res.status(400).json({
            message: "Error Change User's Password!!!",
            error: error.message,
        });
    }
}

module.exports = {
    getListUser: getListUser,
    getUserById: getUserById,
    updateUserId: updateUserId,
    changePassword: changePassword
}