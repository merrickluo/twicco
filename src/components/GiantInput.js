import React from 'react'

import {
  View,
  Text,
  TextInput,
} from 'react-native'

export default class GiantInput extends React.Component {
  render() {
    return (
      <View style={styles.container}>
        <TextInput
          style={styles.input}
          multiline
          textAlignVertical="top"
          underlineColorAndroid="transparent"
          value={this.props.text}
          onChangeText={this.props.onChangeText}
        />
        <Text style={styles.counter}>{this.props.count}</Text>
      </View>
    )
  }
}

const styles = {
  container: {
    flex: 1,
    padding: 8,
    flexDirection: 'row',
  },
  input: {
    fontSize: 16,
    padding: 10,
    borderWidth: 1,
    backgroundColor: 'white',
    flex: 1,
  },
  counter: {
    marginLeft: 8,
    color: 'white',
    fontSize: 16,
  },
}
