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
        />
        <Text style={styles.counter}>140</Text>
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
