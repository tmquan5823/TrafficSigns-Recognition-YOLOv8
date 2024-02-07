const models = require('../../models');

const createSign = async (newSign) => {
    try {
        await models.Sign.create(newSign);
    } catch (error) {
        console.error("Error creating sign:", error);
        throw error; // Rethrow the error to let the caller handle it
    }
};

const showAllSigns = async () => {
    try {
        return await models.Sign.findAll({
            where: { deletedAt: null },
            attributes: [
                'id',
                'code',
                'content',
                'url'
            ]
        });
    } catch (error) {
        console.error("Error getting all signs:", error);
        throw error; // Rethrow the error to let the caller handle it
    }
};

module.exports = {
    addSign: createSign,
    showAllSigns: showAllSigns,
};
