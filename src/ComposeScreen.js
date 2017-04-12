import React from 'react'
import {
  View,
  Text,
  TouchableOpacity,
} from 'react-native'

import ToolBar from './ToolBar.js'
import GiantInput from './components/GiantInput.js'
import Icon from 'react-native-vector-icons/MaterialCommunityIcons'
import ImageButton from './components/ImageButton.js'

export default class ComposeScreen extends React.Component {
  render() {
    return (
      <View style={styles.container}>
        <ToolBar />
        <View style={styles.inputContainer}>
          <GiantInput />
        </View>
        <View style={styles.buttonsContainer}>
          <View style={styles.leftButtons}>
            <ImageButton name="folder-multiple-image" />
            <ImageButton name="map-marker" />
          </View>
          <TouchableOpacity style={styles.tweetButton}>
            <Icon name="comment" color="white" size={24} />
            <Text style={styles.buttonText}>Tweet</Text>
          </TouchableOpacity>
        </View>
      </View>
    )
  }
}

const styles = {
  container: {
    flex: 1,
    backgroundColor: '#222',
    flexDirection: 'column',
  },
  inputContainer: {
    flex: 1,
  },
  buttonsContainer: {
    padding: 12,
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  leftButtons: {
    flexDirection: 'row',
  },
  tweetButton: {
    flexDirection: 'row',
    marginRight: 8,
  },
  buttonText: {
    marginLeft: 8,
    color: 'white'
  },
}
