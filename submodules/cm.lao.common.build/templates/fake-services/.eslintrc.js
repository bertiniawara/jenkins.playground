module.exports = {
  root: true,
  env: {
    es6: true
  },
  "extends": [
  ],
  rules: {
    // 'no-console': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    "no-console": "off",
    "no-debugger": process.env.NODE_ENV === "production" ? "error" : "off",
    "space-before-function-paren": ["error", "never"],
    "semi": ["error", "always", { "omitLastInOneLineBlock": true }],
    "quotes": ["error", "double"],
    "space-in-parens": ["error", "never"],
    "computed-property-spacing": ["error", "never"],
    "array-bracket-spacing": ["error", "never"],
    "lines-around-comment": ["error", {
      "beforeBlockComment": false,
      "afterBlockComment": false
    }],
    "max-len": ["error", {
      "code": 100,
      "ignoreStrings": true,
      "ignoreComments": true,
      "ignoreUrls": true
    }],
    "no-useless-escape": "off"
  },
  parserOptions: {
    parser: "babel-eslint"
  }
};
