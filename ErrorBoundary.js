import React, { type Node, type ComponentType } from 'react'
import {View} from 'react-native';

type Props = {
    children: Node,
    onError?: Function
}

type State = { error: Error | null, hasError: boolean }

class ErrorBoundary extends React.Component<Props, State> {
    state = { error: null, hasError: false }

    static getDerivedStateFromError (error: Error) {
        return { error, hasError: true }
    }

    componentDidCatch (error: Error, info: { componentStack: string }) {
        if (typeof this.props.onError === 'function') {
            this.props.onError.call(this, error, info.componentStack)
        }
    }

    resetError: Function = () => {
        this.setState({ error: null, hasError: false })
    }

    render () {
        return this.state.hasError ?  <View/> : this.props.children
    }
}

export default ErrorBoundary