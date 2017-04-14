module.exports = {
  entry: './main.js',

  output: {
    path: __dirname,
    filename: 'dialer.js',
  },

  module: {
    rules: [
      {
        test: /\.js$/,
        loader: 'babel-loader',
        exclude: /node_modules/
      },

    ],
    loaders: [
      { test: /\.css$/, loader: 'style!css' },
      { test: /\.js$/, loader: 'babel-loader', exclude: /node_modules/}
    ]
  }
}