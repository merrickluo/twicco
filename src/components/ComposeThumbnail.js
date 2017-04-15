import React from 'react'
import { Image, TouchableOpacity } from 'react-native'

export default class ComposeThumbnail extends React.Component {
  handlePress = () => {
    this.props.onPress(this.props.uri)
  }

  render() {
    return (
      <TouchableOpacity onPress={this.handlePress}>
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
