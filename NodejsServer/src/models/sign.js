'use strict';

const { toLocaleString } = require(`../helpers/datetime`)

const {
  Model
} = require('sequelize');
module.exports = (sequelize, DataTypes) => {
  class Sign extends Model {
    /**
     * Helper method for defining associations.
     * This method is not a part of Sequelize lifecycle.
     * The `models/index` file will call this method automatically.
     */
    static associate(models) {
      // define association here
      Sign.hasMany(models.Position, { foreignKey: 'code' })
    }
  }
  Sign.init({
    code: DataTypes.STRING,
    content: DataTypes.STRING,
    url: DataTypes.STRING,
    deletedAt: {
      type: DataTypes.DATE,
      get: function () {
        if (this.getDataValue('deletedAt')) {
          return toLocaleString(this.getDataValue('deletedAt'))
        }
        return null
      },
    },
    createdAt: {
      type: DataTypes.DATE,
      get: function () {
        if (this.getDataValue('createdAt')) {
          return toLocaleString(this.getDataValue('createdAt'))
        }
        return null
      },
    },
    updatedAt: {
      type: DataTypes.DATE,
      get: function () {
        if (this.getDataValue('updatedAt')) {
          return toLocaleString(this.getDataValue('updatedAt'))
        }
        return null
      },
    },
  }, {
    sequelize,
    modelName: 'Sign',
  });
  return Sign;
};