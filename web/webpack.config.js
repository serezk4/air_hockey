const path = require('path');

module.exports = {
    entry: './src/main.js',
    mode: 'development',
    devServer: {
        static: './',
        port: 8080,
    },
    output: {
        filename: 'bundle.js',
        path: path.resolve(__dirname, 'dist'),
        clean: true,
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env'],
                    },
                },
            },
            {
                test: /\.(png|jpg|gif)$/i,
                type: 'asset/resource',
                generator: {
                    filename: 'assets/[name][ext][query]'
                }
            }
        ],
    },
};

