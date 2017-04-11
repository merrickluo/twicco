import React from 'react'

import { Text } from 'react-native'

export default class Tweet extends React.Component {
  render() {
    return (<Text style={styles.text}>{this.props.text}</Text>)
  }
}

const styles = {
  text: {
    color: 'white',
    fontSize: 16,
  }
}
