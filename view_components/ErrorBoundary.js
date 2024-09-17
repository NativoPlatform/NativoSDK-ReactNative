import React from 'react'
import {View} from 'react-native';

class ErrorBoundary extends React.Component {
    constructor(props) {
        super(props);
        this.state = { error: null, hasError: false };
        this.resetError = this.resetError.bind(this);
    }

    static getDerivedStateFromError(error) {
        return { error, hasError: true };
    }

    componentDidCatch(error, info) {
        if (typeof this.props.onError === 'function') {
            this.props.onError.call(this, error, info.componentStack);
        }
    }

    resetError() {
        this.setState({ error: null, hasError: false });
    }

    render() {
        return this.state.hasError ? <View /> : this.props.children;
    }
}

export default ErrorBoundary