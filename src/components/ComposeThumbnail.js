import React from 'react'
import { Image, TouchableOpacity } from 'react-native'

export default class ComposeThumbnail extends React.Component {
  handlePress = () => {
    this.props.onPress(this.props.uri, this.props.index)
  }

  handleLongPress = () => {
    this.props.onLongPress(this.props.uri, this.props.index)
  }

  render() {
    return (
      <TouchableOpacity
        onPress={this.handlePress}
        onLongPress={this.handleLongPress}
      >
        <Image
          source={{ uri: this.props.uri }}
          style={styles.thumbnail}
        />
      </TouchableOpacity>
    )
  }
}

const styles = {
  thumbnail: {
    width: 24,
    height: 24,
    marginLeft: 8,
  }
}
